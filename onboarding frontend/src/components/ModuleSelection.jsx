import { useState, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-hot-toast";
import { ChevronDown } from "lucide-react";
import axios from "../config/axiosConfig";

const ModuleSelection = ({ modules, setLoading }) => {
  const [expandedModules, setExpandedModules] = useState({});
  const [selectedModules, setSelectedModules] = useState([]);
  const navigate = useNavigate();

  // Create a flat registry of all modules for quick lookup
  const moduleRegistry = useMemo(() => {
    const registry = {};
    const buildRegistry = (moduleList, parent = null) => {
      moduleList.forEach((module) => {
        registry[module.id] = { ...module, parent };
        if (module.subModules) buildRegistry(module.subModules, module.id);
      });
    };
    buildRegistry(modules);
    return registry;
  }, [modules]);

  // Get all descendant IDs of a module (including itself)
  const getAllDescendantIds = (moduleId) => {
    const module = moduleRegistry[moduleId];
    if (!module) return [];
    let ids = [moduleId];
    if (module.subModules) {
      module.subModules.forEach((sub) => {
        ids = [...ids, ...getAllDescendantIds(sub.id)];
      });
    }
    return ids;
  };

  // Get all ancestor IDs of a module (including itself)
  const getAllAncestorIds = (moduleId) => {
    const ancestors = [];
    let current = moduleRegistry[moduleId];
    while (current) {
      ancestors.push(current.id);
      current = moduleRegistry[current.parent];
    }
    return ancestors;
  };

  // Check if a module is fully selected (including all descendants)
  const isModuleFullySelected = (moduleId) => {
    const descendantIds = getAllDescendantIds(moduleId);
    return descendantIds.every((id) => selectedModules.includes(id));
  };

  // Check if a module is partially selected (some descendants are selected)
  const isModulePartiallySelected = (moduleId) => {
    const descendantIds = getAllDescendantIds(moduleId);
    return (
      descendantIds.some((id) => selectedModules.includes(id)) &&
      !isModuleFullySelected(moduleId)
    );
  };

  // Handle module selection/deselection
  const handleModuleSelection = (moduleId) => {
    const descendantIds = getAllDescendantIds(moduleId);
    const ancestorIds = getAllAncestorIds(moduleId);

    if (isModuleFullySelected(moduleId)) {
      // Deselect the module and all its descendants
      setSelectedModules((prev) =>
        prev.filter((id) => !descendantIds.includes(id))
      );
    } else {
      // Remove any existing descendants or ancestors from the selection
      const cleanSelection = selectedModules.filter(
        (id) =>
          !descendantIds.includes(id) && !ancestorIds.includes(id)
      );

      // Add the selected module (highest-level parent)
      setSelectedModules([...cleanSelection, moduleId]);
    }
  };

  // Save selected modules to the backend
  const saveSelectedModules = async () => {
    try {
      setLoading(true);
      console.log(selectedModules); // Log the selected modules
      // Example of API call (Uncomment in actual use case)
      const response = await axios.post("/api/opted-modules", selectedModules);
      if (response.data) {
        localStorage.removeItem("optedmodules");
        localStorage.removeItem("token");
        toast.success("Modules saved successfully");
        navigate("/login");
      }
    } catch (error) {
        console.log(error)
      toast.error(error.response?.data?.message || "Something went wrong");
    } finally {
      setLoading(false);
    }
  };

  // Recursive function to render modules and submodules
  const renderModules = (moduleList, level = 0) => {
    return moduleList.map((module) => {
      const isSelected = isModuleFullySelected(module.id);
      const isPartiallySelected = isModulePartiallySelected(module.id);

      return (
        <div key={module.id} className={`ml-${level * 4}`}>
          <div className="flex items-center py-2 border-l-2 border-gray-200 pl-2 flex gap-5">
            <div>
            <input
              type="checkbox"
              checked={isSelected}
              ref={(el) => {
                  if (el) {
                      el.indeterminate = isPartiallySelected;
                    }
                }}
                onChange={() => handleModuleSelection(module.id)}
                className="mr-2"
                />
            <span className="">
              {module.name}
            </span>
            </div>
            {module.subModules && module.subModules.length > 0 && (
              <ChevronDown
                className={`w-4 h-4 cursor-pointer ml-2 ${
                  expandedModules[module.id] ? "transform rotate-180" : ""
                }`}
                onClick={() =>
                  setExpandedModules((prev) => ({
                    ...prev,
                    [module.id]: !prev[module.id],
                  }))
                }
              />
            )}
          </div>
          {expandedModules[module.id] && module.subModules && (
            <div className="ml-4">{renderModules(module.subModules, level + 1)}</div>
          )}
        </div>
      );
    });
  };

  return (
    <div className="p-6 bg-white rounded-lg shadow-md">
      <h1 className="text-2xl font-bold mb-4">Select Modules</h1>
      <div className="mb-4">{renderModules(modules)}</div>
      <button
        onClick={saveSelectedModules}
        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
      >
        Save Selected Modules
      </button>
    </div>
  );
};

export default ModuleSelection;