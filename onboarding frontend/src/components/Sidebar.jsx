import { useState, useEffect } from "react"
import { ChevronDown, Check } from "lucide-react"
import axios from "../config/axiosConfig"
import { useNavigate } from "react-router-dom"
import { toast } from "react-hot-toast"

function Sidebar({ onModuleSelect, optedModuleIds, setOptedModuleIds, modules, userAlreadyOptedModules, setLoading }) {
  const [expandedModules, setExpandedModules] = useState({})
  const navigate = useNavigate()

  async function saveOptedModules() {
    const parentModuleIds = Object.keys(optedModuleIds)
    const data = parentModuleIds.map((parentModuleId) => ({
      parentModuleId: Number(parentModuleId),
      subModuleIds: optedModuleIds[parentModuleId] || [],
    }))

    try {
      setLoading(true)
      const response = await axios.post("/api/opted-modules", data)
      if (response.data) {
        localStorage.removeItem("optedModules")
        localStorage.removeItem("token")
        toast.success("Modules saved successfully")
        navigate("/login")
      }
    } catch (error) {
      toast.error(error.response.data.message || "Something went wrong")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    localStorage.setItem("optedModules", JSON.stringify(optedModuleIds))
  }, [optedModuleIds])

  // Handle toggle of module expansion (when arrow is clicked)
  const toggleModuleExpand = (moduleId) => {
    setExpandedModules((prev) => ({
      ...prev,
      [moduleId]: !prev[moduleId], // Toggle the expanded state of the module
    }))
  }

  // Recursive function to render submodules
  const renderSubModules = (module, parentId) => {
    return (
      <div className="mt-1 space-y-1 border-l border-gray-200 ml-2 pl-2">
        {module.subModules && module.subModules.length > 0 ? (
          module.subModules.map((subModule) => (
            <div key={subModule.id}>
              <button
                className={`w-full text-left py-1.5 text-sm flex items-center justify-between ${
                  optedModuleIds[parentId]?.includes(subModule.id)
                    ? "text-blue-600 font-medium"
                    : "text-gray-600 hover:text-gray-900"
                }`}
                onClick={() => {
                  onModuleSelect({ ...subModule, parentModuleId: parentId })
                  if (subModule.subModules && subModule.subModules.length > 0) {
                    toggleModuleExpand(subModule.id)
                  }
                }}
              >
                <div className="flex items-center gap-2">
                  {optedModuleIds[parentId]?.includes(subModule.id) && <Check className="w-3 h-3 flex-shrink-0" />}
                  <span className="truncate">{subModule.name}</span>
                </div>
                {subModule.subModules && subModule.subModules.length > 0 && (
                  <ChevronDown
                    className={`w-4 h-4 transition-transform ${expandedModules[subModule.id] ? "rotate-180" : ""}`}
                  />
                )}
              </button>
              {/* Recursively render submodules for this submodule */}
              {expandedModules[subModule.id] && renderSubModules(subModule, parentId)}
            </div>
          ))
        ) : (
          <p className="py-1.5 text-sm text-gray-400">No submodules available</p>
        )}
      </div>
    )
  }



  const menuItems = [
    { label: "Chat Room", href: "/chat" },
  ]

  return (
    <div className="w-64 bg-white border-r min-h-screen overflow-y-auto">
      <div className="p-4">
        <img src="../assets/images/logo.png" alt="Pisoft Logo" className="w-32 mb-8" />



        {/* selected modules section  */}
        <nav className="space-y-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-sm font-semibold text-gray-700">Modules</h2>
            {!userAlreadyOptedModules && (
              <button
              onClick={saveOptedModules}
                className="px-3 py-1.5 rounded-md text-sm font-medium text-white bg-blue-500 hover:bg-blue-600 transition-colors"
              >
                Save
              </button>
            )}
          </div>
          {Object.entries(modules).map(([name, module]) => (
            <div key={name} className="space-y-1 border-l-2 border-gray-200 pl-3">
              <button
                onClick={() => {
                  toggleModuleExpand(module.id)
                  onModuleSelect({ name, ...module })
                }}
                className={`w-full flex items-center justify-between py-2 transition-colors ${
                  optedModuleIds[module.id] ? "text-blue-600 font-medium" : "text-gray-700 hover:text-gray-900"
                }`}
                >
                <div className="flex items-center gap-2 text-sm">
                  {optedModuleIds[module.id] && <Check className="w-4 h-4 flex-shrink-0" />}
                  <span className="truncate">{module.name}</span>
                </div>
                {module.subModules && module.subModules.length > 0 && (
                  <ChevronDown
                    className={`w-4 h-4 transition-transform ${expandedModules[module.id] ? "rotate-180" : ""}`}
                    />
                )}
              </button>
              {/* Render the submodules only when expanded */}
              {expandedModules[module.id] && renderSubModules(module, module.id)}
            </div>
          ))}
        </nav>
        {/* selected modules end  */}


      {/* menu items */}

      {
        menuItems && menuItems.length > 0 && menuItems.map((item, index) => (
          <a
          key={index}
          href={item.href}
          className="flex items-center gap-3 px-4 py-3 text-gray-600 hover:bg-gray-100 rounded-lg"
          >
            {/* <item.icon className="w-5 h-5" /> */}
            <h2 className="text-sm font-semibold text-gray-700">{item.label}</h2>
          </a>
        ))
      }

      {/* menu items end */}
      </div>
    </div>
  )
}

export default Sidebar

