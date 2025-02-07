const UserAvatar = ({ user }) => {
    if (user.avatar) {
      return (
        <img src={user.avatar || "/placeholder.svg"} alt={user.userName} className="w-10 h-10 rounded-full shadow-md" />
      )
    }
    return (
      <div className="w-10 h-10 rounded-full bg-gradient-to-r from-blue-400 to-blue-600 flex items-center justify-center text-white font-bold shadow-md">
        {user.userName?.charAt(0).toUpperCase()}
      </div>
    )
  }
  
  export default UserAvatar
  
  