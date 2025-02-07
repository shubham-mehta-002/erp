function Navbar() {
  const name = localStorage.getItem("name")

  const handleLogout = () => {
    // Remove items from localStorage
    localStorage.removeItem("token")
    localStorage.removeItem("userId")
    localStorage.removeItem("name")

    // Navigate to login page (adjust the route as needed)
    navigate("/login")
  }


    return (
      <nav className="bg-[#457B9D] text-white p-4 shadow-md">
        <div className="container mx-auto flex justify-between items-center">
          <h1 className="text-xl font-semibold">Pisoft Informatics Pvt. Ltd.</h1>
          <div className="flex items-center space-x-4">
            <span className="text-sm">[04-02-2025]</span>
            <button className="text-xl">🔔</button>
            <div className="flex items-center space-x-2">
              <span className="text-sm">{name}</span>
              {/* <svg
                className="w-4 h-4"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
              </svg> */}
              <button
                onClick={handleLogout}
                className="bg-red-500 hover:bg-red-800 text-white font-semibold py-1 px-2 rounded transition duration-300"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </nav>
    )
  }
  
  export default Navbar
  
  