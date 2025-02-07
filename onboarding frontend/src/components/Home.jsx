// import { useState , useEffect} from "react"
// import {
//   LayoutDashboard,
//   User,
//   FileEdit,
//   Send,
//   Clock,
//   Users,
//   Calendar,
//   ChevronDown,
//   Briefcase,
//   DollarSign,
//   GraduationCap,
// } from "lucide-react"
// import Navbar from "./Navbar"
// import MainContent from "./MainContent"
// import axios from "../config/axiosConfig"
// import { useNavigate } from "react-router-dom"
// import {toast} from "react-hot-toast"
// import Sidebar from "./Sidebar"
// function ModuleList({ onModuleSelect ,optedModuleIds,setOptedModuleIds,modules,userAlreadyOptedModules}) {
//     const [expandedModule, setExpandedModule] = useState(null)
//     // const [modules,setModules] = useState([])
//     const navigate = useNavigate()

//     // useEffect(()=>{
//     //     async function fetchModules(){
//     //         try{
//     //             const response = await axios.get("/modules/all")
//     //             console.log({response})
//     //             setModules(response.data);
//     //         }catch(error){
//     //             toast.error(error.response.data.message || "Couldn't fetch module list")
//     //         }
//     //     }
//     //     fetchModules()

//     // },[])
  
//     // const modules = {
//     //   HR: {
//     //     id:1,
//     //     icon: Briefcase,
//     //     subModules: [
//     //       { id:2, name: "Assign Designation", description: "Manage employee designations" },
//     //       { id:3, name: "Add Trainee", description: "Add new trainees to the system" },
//     //       { id:4, name: "Add Employee", description: "Onboard new employees" },
//     //     ],
//     //   },
//     //   Finance: {
//     //     id:5,
//     //     icon: DollarSign,
//     //     subModules: [
//     //       { id:6,name: "Payroll", description: "Manage employee salaries" },
//     //       { id:7,name: "Expenses", description: "Track company expenses" },
//     //       { id:8,name: "Budget", description: "Manage department budgets" },
//     //     ],
//     //   },
//     //   Training: {
//     //     id:9,
//     //     icon: GraduationCap,
//     //     subModules: [
//     //       { id:10,name: "Course Management", description: "Manage training courses" },
//     //       { id:11,name: "Schedule Training", description: "Plan training sessions" },
//     //       { id:12,name: "Training Reports", description: "View training analytics" },
//     //     ],
//     //   },
//     //   Dummy: {
//     //     id:13,
//     //     icon: GraduationCap,
//     //     subModules: [ ],
//     //   },
//     // }
  
//     const menuItems = [
//     //   { icon: LayoutDashboard, label: "Dashboard", href: "#" },
//     //   { icon: User, label: "My Profile", href: "#" },
//     //   { icon: FileEdit, label: "Update Profile", href: "#" },
//       // { icon: Send, label: "Request", href: "#" },
//       // { icon: Clock, label: "My Attendance", href: "#" },
//       // { icon: Users, label: "Student Section", href: "#" },
//       // { icon: Calendar, label: "Leave Management", href: "#" },
//     ]
    
//     async function saveOptedModules(){
//         const parentModuleIds = Object.keys(optedModuleIds)
//         const data = []
//         parentModuleIds.forEach(parentModuleId => {
//             const subModuleIds = optedModuleIds[parentModuleId] || []
//             data.push({ parentModuleId : Number(parentModuleId) , subModuleIds })
//         })
//         console.log({data})
        

//         try{
//             const response=await axios.post("/api/opted-modules",
//                 data
//             )
//             console.log({response})
//             if(response.data){
//                 localStorage.removeItem("optedModules")
//                 localStorage.removeItem("token")
//                 toast.success("Modules saved successfully")
//                 navigate("/login")
//             }
//         }catch(error){
//             toast.error( error.response.data.message||"Something went wrong")
//             console.log(error)
//         }
//     }

//     return (
//       <div className="w-64 bg-white border-r min-h-screen overflow-y-auto">
//         <div className="p-4">
//           <img src="/logo.png" alt="Pisoft Logo" className="w-32 mb-8" />
//           <nav className="space-y-1">
//             {menuItems.map((item, index) => (
//               <a
//                 key={index}
//                 href={item.href}
//                 className="flex items-center gap-3 px-4 py-3 text-gray-600 hover:bg-gray-100 rounded-lg"
//               >
//                 {/* <item.icon className="w-5 h-5" /> */}
//                 <span>{item.label}</span>
//               </a>
//             ))}
  
//             <div className="pt-4">
//               <div className="flex items-center justify-between mb-2"><div className="px-4 py-2 text-sm font-medium text-gray-500">Modules</div> 
//               {!userAlreadyOptedModules && <button  onClick={ saveOptedModules} className="px-2 py-1 rounded-xl cursor-pointer border-2 border-solid text-sm font-medium text-gray-500 bg-gray-200 ">SAVE</button> }
//               </div>
//               {Object.entries(modules).map(([name, module]) => (
//                 <div key={name}>
//                   <button
//                     onClick={() => {
//                       setExpandedModule(expandedModule === name ? null : name)
//                       onModuleSelect({ name, ...module })
//                     }}
//                     className="w-full flex items-center justify-between px-4 py-3 text-gray-600 hover:bg-gray-100 rounded-lg"
//                   >
//                     <div className="flex items-center gap-3">
//                       {/* <module.icon className="w-5 h-5" /> */}
//                       <span>{module.name}</span>
//                     </div>
//                     <ChevronDown
//                       className={`w-4 h-4 transition-transform ${expandedModule === name ? "transform rotate-180" : ""}`}
//                     />
//                   </button>
//                   {expandedModule === name && (
//                     <div className="ml-11 space-y-1">
//                       {module.subModules && module.subModules.length > 0 && module.subModules.map((subModule, index) => (
//                         <button
//                           key={index}
//                         //   onClick={() => onModuleSelect({ ...subModule, parent: name })}
//                           className="w-full text-left px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg"
//                         >
//                           {subModule.name}
//                         </button>
//                       ))}
  
//                       {module.subModules && module.subModules.length == 0 && (
//                         <p className="w-full text-left px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg">
//                           No submodule available
//                         </p>
//                       )}
//                     </div>
//                   )}
//                 </div>
//               ))}
//             </div>
//           </nav>
//         </div>
//       </div>
//     )
// }
  
  
import { useState, useEffect } from "react"
import Navbar from "./Navbar"
import MainContent from "./MainContent"
import Sidebar from "./Sidebar"
import ModuleSelection from "./ModuleSelection"
import axios from "../config/axiosConfig"
import { toast } from "react-hot-toast"

export default function Home({ setLoading }) {
  const [selectedModule, setSelectedModule] = useState(null)
  const [optedModuleIds, setOptedModuleIds] = useState({})
  const [modules, setModules] = useState([])
  const [userAlreadyOptedModules, setUserAlreadyOptedModules] = useState(false)

  async function fetchOptedModules() {
    try {
      const response = await axios.get("/api/opted-modules")
      return response.data
    } catch (error) {
      toast.error(error.response?.data?.message || "Couldn't fetch opted modules")
    }
  }

  async function fetchModules() {
    try {
      const response = await axios.get("/modules/all")
      return response.data
    } catch (error) {
      toast.error(error.response?.data?.message || "Couldn't fetch module list")
    }
  }

  async function loadModules() {
    try {
      setLoading(true)
      const optedModules = await fetchOptedModules()
      if (optedModules && optedModules.length > 0) {
        setUserAlreadyOptedModules(true)
        setModules(optedModules)
      } else {
        const allModules = await fetchModules()
        setModules(allModules)
        setUserAlreadyOptedModules(false)
        setOptedModuleIds(localStorage.getItem("optedModules") ? JSON.parse(localStorage.getItem("optedModules")) : {})
      }
    } catch (error) {
      console.error(error)
      setUserAlreadyOptedModules(false)
      toast.error(error.response?.data?.message || "Couldn't fetch modules")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadModules()
  }, [])

  return (
    <>
      <Navbar />
      {userAlreadyOptedModules ? (
        <div className="flex">
          {modules.length > 0 && (
            <Sidebar
              onModuleSelect={setSelectedModule}
              optedModuleIds={optedModuleIds}
              setOptedModuleIds={setOptedModuleIds}
              modules={modules}
              userAlreadyOptedModules={userAlreadyOptedModules}
              setLoading={setLoading}
            />
          )}
          <MainContent
            selectedModule={selectedModule}
            optedModuleIds={optedModuleIds}
            setOptedModuleIds={setOptedModuleIds}
            userAlreadyOptedModules={userAlreadyOptedModules}
          />
        </div>
      ) : (
        <ModuleSelection modules={modules} setLoading={setLoading} />
      )}
    </>
  )
}

