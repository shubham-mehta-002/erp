import UserTile from "./UserTile"

const ChatSidebar = ({ activeChats, availableUsers, onChatSelect, onUserSelect, selectedChat }) => (
<div className="w-1/4 bg-white p-6 shadow-lg">
    <h2 className="text-2xl font-bold mb-6 text-gray-800">Chats</h2>
    <div className="mb-8">
      <h3 className="text-lg font-semibold mb-3 text-gray-700">Active Chats</h3>
      <div className="space-y-2 overflow-y-auto max-h-[30vh]">
        {activeChats.map((chat) => (
          <UserTile
            key={chat.chatId}
            user={chat}
            onClick={onChatSelect}
            isActive={selectedChat?.chatId === chat.chatId}
          />
        ))}
      </div>
    </div>
    <div>
      <h3 className="text-lg font-semibold mb-3 text-gray-700">Available Users</h3>
      <div className="space-y-2 overflow-y-auto max-h-[30vh]">
        {availableUsers.map((user) => (
          <UserTile
            key={user.id}
            user={user}
            onClick={onUserSelect}
            isActive={selectedChat?.id === user.id && selectedChat.status === "NEW"}
          />
        ))}
      </div>
    </div>
  </div>
)

export default ChatSidebar

