import React, { useState } from 'react'
import Navbar from '../components/Navbar'
import { useSearchParams , useNavigate} from "react-router-dom";
import {toast} from "react-hot-toast"
import axios from "../config/axiosConfig"
import CircularProgress from '@mui/material/CircularProgress';

export default function SetPassword({setLoading}) {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: ''
  })
  const [errors, setErrors] = useState({})
  const [searchParams] = useSearchParams();

  const token = searchParams.get("token");
  const email = searchParams.get("email");
  const navigate = useNavigate()
  

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }))
  }

  const validateForm = () => {
    let tempErrors = {}
    if (!formData.password) {
      tempErrors.password = "Password is required"
    } else if (formData.password.length < 6) {
      tempErrors.password = "Password must be at least 6 characters"
    }
    if (formData.password !== formData.confirmPassword) {
      tempErrors.confirmPassword = "Passwords do not match"
    }
    setErrors(tempErrors)
    return Object.keys(tempErrors).length === 0
  }

  const handleSubmit = async(e) => {
    e.preventDefault()
    if (validateForm()) {

        try{
            setLoading(true)
            const response = await axios.post("/set-password", {
                token,
                password: formData.password,
                email
            })
            toast.success("Password set successfully")
            navigate("/login")
        }catch(error){
            toast.error(error.response.data.message ||"Something went wrong")
        }finally{
            setLoading(false)
        }
    }else {
        toast.error("Form is invalid")
    }
  }


  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <div className="max-w-md mx-auto mt-20 p-6 bg-white rounded-lg shadow-md">
        <p>Hello {email} , set your password</p>
        <h1 className="text-2xl font-semibold mb-8 text-center">Set Password</h1>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Password
            </label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className={`w-full px-3 py-2 border ${errors.password ? 'border-red-500' : 'border-gray-300'} rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500`}
              placeholder="Enter password"
            />
            {errors.password && <p className="mt-1 text-xs text-red-500">{errors.password}</p>}
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Confirm Password
            </label>
            <input
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              className={`w-full px-3 py-2 border ${errors.confirmPassword ? 'border-red-500' : 'border-gray-300'} rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500`}
              placeholder="Confirm password"
            />
            {errors.confirmPassword && <p className="mt-1 text-xs text-red-500">{errors.confirmPassword}</p>}
          </div>
          <button
            type="submit"
            className="w-full bg-[#4F87AA] text-white py-2 px-4 rounded-md hover:bg-[#3e6d89] transition-colors"
          >
            Set Password
          </button>
        </form>
      </div>
    </div>
  )
}
