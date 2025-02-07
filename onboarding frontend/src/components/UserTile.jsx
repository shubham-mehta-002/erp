import UserAvatar from "./UserAvatar"

const UserTile = ({ user, onClick, isActive }) => (
  <div
    onClick={() => onClick(user)}
    className={`flex items-center p-3 hover:bg-gray-100 cursor-pointer rounded-lg transition-colors ${
      isActive ? "bg-blue-100" : ""
    }`}
  >
    <UserAvatar user={user} />
    <span className="ml-3 font-medium">{user.userName}</span>
  </div>
)

export default UserTile

