import {useNavigate} from "react-router-dom"
import { useEffect } from "react"

export default function ProtectedRoute({ children }) {
    const navigate=useNavigate()

    useEffect(()=>{
        if(!localStorage.getItem("token")){
            navigate("/login")
            return;
        }
    },[])
    
    return (
        <div>
            {children}
        </div>
    )
}