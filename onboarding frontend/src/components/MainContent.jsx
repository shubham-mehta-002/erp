import { useState, useEffect } from "react"
import { Check } from "lucide-react"

export default function MainContent({ selectedModule, optedModuleIds, setOptedModuleIds, userAlreadyOptedModules }) {
//   const handleModuleSelection = (parentModuleId, subModuleId = null) => {
//     setOptedModuleIds((prev) => {
//       let updatedModules = { ...prev }

//       if (subModuleId === null) {
//         if (updatedModules[parentModuleId]) {
//           delete updatedModules[parentModuleId]
//         } else {
//           updatedModules[parentModuleId] = selectedModule.subModules?.map(sub => sub.id) || []
//         }
//       } else {
//         let subModules = updatedModules[parentModuleId] ? [...updatedModules[parentModuleId]] : []

//         if (subModules.includes(subModuleId)) {
//           subModules = subModules.filter(id => id !== subModuleId)
//         } else {
//           subModules.push(subModuleId)
//         }

//         if (subModules.length === 0) {
//           delete updatedModules[parentModuleId]
//         } else {
//           updatedModules[parentModuleId] = subModules
//         }
//       }

//       return updatedModules
//     })
//   }

const handleModuleSelection = ({moduleId}) => {
    setOptedModuleIds((prev) => {
      let updatedModules = { ...prev }

      if (updatedModules.contains(moduleId)) {
        delete updatedModules[moduleId]
      } else {
        updatedModules[moduleId] = selectedModule.subModules?.map(sub => sub.id) || []
      }

      return updatedModules
    })
}

  if (!selectedModule) {
    return (
      <div className="flex w-full">
        <main className="flex-1 p-6 bg-gray-50 w-full">
          <h1 className="text-2xl font-semibold mb-6">Select a Module</h1>
          <p className="text-gray-600">Please select a module from the sidebar to view its details.</p>
        </main>
      </div>
    )
  }

  return (
    <main className="flex-1 p-6 bg-gray-50">
      <div className="bg-white rounded-lg shadow p-6">
        <div className="space-y-6">
          {/* Parent Module Selection */}
          <div className="flex justify-between items-center">
            <div>
              <h2 className="text-xl font-medium text-gray-800">{selectedModule.name}</h2>
              <p className="text-gray-600 mt-1">{selectedModule.description}</p>
            </div>
            {!userAlreadyOptedModules && (
              <div className="relative">
                <div
                  onClick={() => handleModuleSelection(selectedModule.id)}
                  className={`w-5 h-5 border-2 rounded-md flex items-center justify-center cursor-pointer transition-colors ${
                    optedModuleIds[selectedModule.id]
                      ? 'bg-blue-500 border-blue-500'
                      : 'border-gray-300 hover:border-blue-300'
                  }`}
                >
                  {optedModuleIds[selectedModule.id] && (
                    <Check className="w-4 h-4 text-white" />
                  )}
                </div>
              </div>
            )}
          </div>
          <hr className="border-gray-200" />

          {/* Submodules List */}
          {selectedModule.subModules?.map((subModule) => (
            <div key={subModule.id} className="flex justify-between items-center ml-6">
              <div>
                <h2 className="text-lg font-medium text-gray-800">{subModule.name}</h2>
                <p className="text-gray-600 mt-1">{subModule.description || "No description available"}</p>
              </div>
              {!userAlreadyOptedModules && (
                <div className="relative">
                  <div
                    onClick={() => handleModuleSelection(selectedModule.id, subModule.id)}
                    className={`w-5 h-5 border-2 rounded-md flex items-center justify-center cursor-pointer transition-colors ${
                      optedModuleIds[selectedModule.id]?.includes(subModule.id)
                        ? 'bg-blue-500 border-blue-500'
                        : 'border-gray-300 hover:border-blue-300'
                    }`}
                  >
                    {optedModuleIds[selectedModule.id]?.includes(subModule.id) && (
                      <Check className="w-4 h-4 text-white" />
                    )}
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </main>
  )
}