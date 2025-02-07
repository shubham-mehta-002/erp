import { useState, useEffect, useRef } from "react"
import axios from "../config/axiosConfig"
import { toast } from "react-hot-toast"
import SockJs from "sockjs-client/dist/sockjs.min.js"
import Stomp from "stompjs"
import ChatSidebar from "../components/ChatSidebar"
import ChatArea from "../components/ChatArea"
import { formatUser, updateChatsAfterNewMessage } from "../utils/chatUtils"

const Chat = () => {
  const [activeChats, setActiveChats] = useState([])
  const [availableUsers, setAvailableUsers] = useState([])
  const [selectedChat, setSelectedChat] = useState(JSON.parse(localStorage.getItem("selectedChat")) || null)
  const [messages, setMessages] = useState([])
  const [inputMessage, setInputMessage] = useState("")
  const [selectedFile, setSelectedFile] = useState(null)
  const [stompClient, setStompClient] = useState(null)
  const messagesEndRef = useRef(null)
  const [fromDate, setFromDate] = useState(new Date().toISOString().split("T")[0])
  const [toDate, setToDate] = useState(new Date().toISOString().split("T")[0])

  const loggedInUserId = localStorage.getItem("userId")
  const loggedInUserName = localStorage.getItem("name")
  const [userData, setUserData] = useState({
    username: loggedInUserName,
    connected: false,
  })
  
  useEffect(() => {
    if (selectedChat) {
      localStorage.setItem("selectedChat", JSON.stringify(selectedChat))
    }
  }, [selectedChat])

  useEffect(() => {
    fetchChatsAndUsers()
  }, [])

  const fetchChatsAndUsers = async () => {
    try {
      const [activeChatsResponse, availableUsersResponse] = await Promise.all([
        axios.get("/api/chat/active"),
        axios.get("/api/chat/available"),
      ])
      setActiveChats(activeChatsResponse.data.map((chat) => formatUser(chat, "EXISTING")))
      setAvailableUsers(availableUsersResponse.data.map((user) => formatUser(user, "NEW")))
    } catch (error) {
      console.error("Error fetching chats and users:", error)
      toast.error("Failed to load chats and users")
    }
  }

  const handleChatSelect = (chat) => {
    setSelectedChat(chat)
    setMessages([])
    fetchChatHistory(chat.chatId, fromDate, toDate)
  }

  const handleUserSelect = (user) => {
    setSelectedChat(user)
    setMessages([])
  }

  const fetchChatHistory = async (chatId, from, to) => {
    try {
      const response = await axios.get(`/api/messages/${chatId}`, {
        params: {
          userId: loggedInUserId,
          from,
          to,
        },
      })
      console.log({response})
      setMessages(response.data)
      setTimeout(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "auto" })
      }, 0)
    } catch (error) {
      console.error("Error fetching chat history:", error)
      toast.error("Failed to load chat history")
    }
  }

  const handleSendMessage = async () => {
    if (!inputMessage.trim() && !selectedFile) return;
    console.log({ inputMessage, selectedFile });
  
    try {
      let filePath = null;
  
      if (selectedFile) {
        const formData = new FormData();
        formData.append("file", selectedFile);
        const uploadResponse = await axios.post("/api/upload", formData);
        filePath = uploadResponse.data.fileUrl; 
        console.log(filePath)
      }
  
      let response;
      if (selectedChat.status === "NEW") {
        console.log(filePath)

        response = await axios.post("/api/chat/single", {
          userId: Number(selectedChat.id),
          content: inputMessage || "",
          filePath: filePath,
        });
  
        if (response.data && response.data.id) {
          const newChat = formatUser(
            {
              ...selectedChat,
              chatId: response.data.id,
            },
            "EXISTING"
          );
          const { updatedActiveChats, updatedAvailableUsers } =
          updateChatsAfterNewMessage(activeChats, availableUsers, newChat);
          setActiveChats(updatedActiveChats);
          setAvailableUsers(updatedAvailableUsers);
          setSelectedChat(newChat);
        }
      } else {
        console.log(filePath)

        response = await axios.post("/api/messages", {
          message: inputMessage || "",
          chatId: selectedChat.chatId,
          filePath: filePath,
        });
      }
  
      if (stompClient && userData.connected) {
        stompClient.send(
          "/app/chat.sendMessage",
          {},
          JSON.stringify({
            messageId: response.data.id
          })
        );
      } else {
        toast.error("WebSocket not connected!");
      }
  
      setMessages((prevMessages) => [...prevMessages, response.data]);
      setInputMessage("");
      setSelectedFile(null);
      messagesEndRef.current?.scrollIntoView({ behavior: "instant" });
    } catch (error) {
      console.error("Error sending message:", error);
      toast.error("Failed to send message");
    }
  };
  
  const handleFileChange = (e) => {
    const file = e.target.files[0]
    setSelectedFile(file)
  }

  useEffect(() => {
    const socket = new SockJs("http://localhost:8080/ws")
    const client = Stomp.over(socket)
    client.connect({}, onConnect, onError)
    setStompClient(client)

    return () => {
      if (client && client.connected) {
        client.disconnect()
      }
    }
  }, [])

  const onConnect = () => {
    setUserData((prev) => ({ ...prev, connected: true }))
    toast.success("Connected to chat server")
  }

  const onError = () => {
    setUserData((prev) => ({ ...prev, connected: false }))
    toast.error("Unable to connect to chat server")
  }

  useEffect(() => {
    if (stompClient && userData.connected && selectedChat?.chatId) {
      stompClient.unsubscribe(`/group/${selectedChat.chatId}`)

      const subscription = stompClient.subscribe(`/group/${selectedChat.chatId}`, onMessageReceive)
      return () => subscription.unsubscribe()
    }
  }, [stompClient, userData.connected, selectedChat])

  const onMessageReceive = (payload) => {
    const message = JSON.parse(payload.body)
    if (message.sender === Number(localStorage.getItem("userId"))) {
      return
    }
    setMessages((prevMessages) => [...prevMessages, message])
  }

  const handleDateChange = (date, type) => {
    if (type === "from") {
      if(date == null || date.trim() === ""){
        date = new Date().toISOString().split("T")[0]
      }
      setFromDate((date == null) ? new Date().toISOString().split("T")[0]: date)
    } else {
      if(date == null || date.trim() === ""){
        date = new Date().toISOString().split("T")[0]
      }
      setToDate((date == null) ? new Date().toISOString().split("T")[0]: date)
    }
  }

  useEffect(() => {
    if (selectedChat) {
      fetchChatHistory(selectedChat.chatId, fromDate, toDate);
    }
  }, [fromDate, toDate, selectedChat]);
  
  return (
    <div className="flex flex-col bg-gray-50 h-[90vh]">
      <div className="flex space-x-4 p-4 bg-white shadow">
        <input
          type="date"
          value={fromDate}
          onChange={(e) => handleDateChange(e.target.value, "from")}
          max={toDate}
          className="border rounded p-2"
        />
        <input
          type="date"
          value={toDate}
          onChange={(e) => handleDateChange(e.target.value, "to")}
          max={new Date().toISOString().split("T")[0]}
          className="border rounded p-2"
        />
      </div>
      <div className="flex flex-1 overflow-hidden">
        <ChatSidebar
          activeChats={activeChats}
          availableUsers={availableUsers}
          onChatSelect={handleChatSelect}
          onUserSelect={handleUserSelect}
          selectedChat={selectedChat}
        />
        {selectedChat ? (
          <ChatArea
            selectedChat={selectedChat}
            messages={messages}
            inputMessage={inputMessage}
            setInputMessage={setInputMessage}
            handleSendMessage={handleSendMessage}
            handleFileChange={handleFileChange}
            selectedFile={selectedFile}
            setSelectedFile={setSelectedFile}
            userData={userData}
            fetchChatHistory={fetchChatHistory}
            fromDate={fromDate}
            toDate={toDate}
          />
        ) : (
          <div className="flex-1 flex items-center justify-center text-gray-500 text-xl">
            Select a user to start chatting
          </div>
        )}
      </div>
    </div>
  )
}

export default Chat