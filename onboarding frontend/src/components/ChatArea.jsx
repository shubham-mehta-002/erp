import { useRef, useEffect } from "react"
import { PaperclipIcon, SendIcon } from "lucide-react"
import UserAvatar from "./UserAvatar"

const ChatArea = ({
    selectedChat,
    messages = [],
    inputMessage,
    setInputMessage,
    handleSendMessage,
    handleFileChange,
    selectedFile,
    setSelectedFile,
}) => {
  const fileInputRef = useRef(null)
  const messagesEndRef = useRef(null)
  const userId = Number(localStorage.getItem("userId"))

  const renderFilePreview = (message) => {
    // Handle file upload preview (before sending)
    console.log(message)
    if (message.file) {
      if (message.file.type.startsWith("image/")) {
        return (
          <div className="relative">
            <img
              src={URL.createObjectURL(message.file)}
              alt="Preview"
              className="max-w-xs max-h-40 rounded-lg shadow"
            />
          </div>
        )
      }
      return (
        <div className="flex items-center space-x-2 bg-gray-100 p-2 rounded-lg">
          <span className="truncate">{message.file.name}</span>
          <a
            href={URL.createObjectURL(message.file)}
            download={message.file.name}
            className="text-blue-500 hover:text-blue-700"
          >
            Download
          </a>
        </div>
      )
    }
    
    // Handle persisted files from server
    if (message.filePath) {
      const extension = message.filePath.split('.').pop().toLowerCase()
      const isImage = ['jpg', 'jpeg', 'png', 'gif', 'webp'].includes(extension)
      
      if (isImage) {
        return (
          <div className="relative">
            <img
              src={message.filePath}
              alt="File Preview"
              className="max-w-xs max-h-40 rounded-lg shadow"
            />
          </div>
        )
      }
      
      return (
        <div className="flex items-center space-x-2 bg-gray-100 p-2 rounded-lg">
          <span className="truncate">{message.filePath.split('/').pop()}</span>
          <a
            href={message.filePath}
            download
            className="text-blue-500 hover:text-blue-700"
          >
            Download
          </a>
        </div>
      )
    }
    
    return null
  }

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "instant" })
  }, [messages])

  const formatTimestamp = (timestamp) => {
    const date = new Date(timestamp)
    return date.toLocaleString()
  }

  return (
    <div className="flex-1 flex flex-col">
      <div className="bg-white p-4 shadow-md flex items-center">
        <UserAvatar user={selectedChat} />
        <h2 className="text-xl font-semibold ml-3 text-gray-800">{selectedChat.userName}</h2>
      </div>
      <div className="flex-1 overflow-y-auto p-6 space-y-4">
        {messages && messages.length > 0 && messages.map((message, index) => (
          <div key={index} className={`flex ${message.sender === userId ? "justify-end" : "justify-start"}`}>
            <div
              className={`max-w-xs p-3 rounded-lg shadow ${
                message.sender === userId ? "bg-blue-500 text-white" : "bg-white"
              }`}
            >
              <p className="mb-1">{message.content}</p>
              {(message.file || message.filePath) && renderFilePreview(message)}
              <p className="text-xs mt-1 opacity-75">{formatTimestamp(message.timestamp)}</p>
            </div>
          </div>
        ))}
        {messages && messages.length === 0 && (
          <p className="text-gray-500 text-center">No messages to show</p>
        )}
        <div ref={messagesEndRef} />
      </div>
      <div className="bg-white p-4 shadow-lg">
        {selectedFile && (
          <div className="p-2 bg-gray-100 rounded-lg mb-2">
            {renderFilePreview({ file: selectedFile })}
            <button
              onClick={() => setSelectedFile(null)}
              className="text-red-500 hover:text-red-700 mt-1 transition-colors"
            >
              Remove
            </button>
          </div>
        )}
        <div className="flex items-center space-x-2">
          <input
            type="text"
            value={inputMessage}
            onChange={(e) => setInputMessage(e.target.value)}
            className="flex-1 border border-gray-300 rounded-full py-2 px-4 focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Type a message..."
            onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()}
          />
          <input type="file" ref={fileInputRef} onChange={handleFileChange} className="hidden" />
          <button
            onClick={() => fileInputRef.current.click()}
            className="p-2 rounded-full hover:bg-gray-100 transition-colors"
          >
            <PaperclipIcon size={20} className="text-gray-600" />
          </button>
          <button
            onClick={handleSendMessage}
            className="bg-blue-500 text-white p-2 rounded-full hover:bg-blue-600 transition-colors"
          >
            <SendIcon size={20} />
          </button>
        </div>
      </div>
    </div>
  )
}

export default ChatArea