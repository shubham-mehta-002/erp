import { useState } from "react"
import Navbar from "../components/Navbar"
import axios from "../config/axiosConfig"
import {useNavigate} from "react-router-dom";
import {toast} from "react-hot-toast"

export default function Login({setLoading}) {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  })
 
  const [errors, setErrors] = useState({})

  const navigate = useNavigate()
  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const validateForm = () => {
    const tempErrors = {}
    if (!formData.email) {
      tempErrors.email = "Email is required"
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      tempErrors.email = "Email is invalid"
    }
    if (!formData.password) {
      tempErrors.password = "Password is required"
    }
    setErrors(tempErrors)
    return Object.keys(tempErrors).length === 0
  }

  const handleSubmit = async(e) => {
    e.preventDefault()
    if (validateForm()) {
      try{
        setLoading(true)
        const response = await axios.post("/auth/login", {
            email : formData.email,
            password : formData.password
        })
        console.log({response})
        toast.success("Login successful")
        localStorage.setItem("token", response.data.jwt)
        localStorage.setItem("userId", response.data.user.id)
        localStorage.setItem("name", response.data.user.name) //  change to name after the db update

        navigate("/")
      }catch(error){
        toast.error(error.response.data.message || "Something went wrong")
      }finally{
        setLoading(false)
      }
    }else {
        toast.error("Form is invalid")
        console.log("Form is invalid")
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-md mx-auto mt-20 p-6 bg-white rounded-lg shadow-md">
        <h1 className="text-2xl font-semibold mb-8 text-center">Login</h1>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email Address</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className={`w-full px-3 py-2 border ${errors.email ? "border-red-500" : "border-gray-300"} rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500`}
              placeholder="Enter your email"
            />
            {errors.email && <p className="mt-1 text-xs text-red-500">{errors.email}</p>}
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className={`w-full px-3 py-2 border ${errors.password ? "border-red-500" : "border-gray-300"} rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500`}
              placeholder="Enter password"
            />
            {errors.password && <p className="mt-1 text-xs text-red-500">{errors.password}</p>}
          </div>
          <button
            type="submit"
            className="w-full bg-[#4F87AA] text-white py-2 px-4 rounded-md hover:bg-[#3e6d89] transition-colors"
          >
            Login
          </button>
        </form>
      </div>
    </div>
  )
}

