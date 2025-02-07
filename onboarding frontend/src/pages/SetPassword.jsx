import Navbar from "../components/Navbar"
import { useNavigate } from "react-router-dom"
// import CircularProgress from '@mui/material/CircularProgress';

export default function SetPassword() {
  const navigate = useNavigate()
  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <div className="max-w-md mx-auto mt-20 p-6 bg-white rounded-lg shadow-md">
        <h1 className="text-2xl font-semibold mb-8 text-center">Set Password</h1>
        <form className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Email Address
            </label>
            <input
              type="email"
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Enter your email"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Password
            </label>
            <input
              type="password"
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Enter password"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Confirm Password
            </label>
            <input
              type="password"
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Confirm password"
            />
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
