export const formatUser = (user, status) => ({
    id: user.id || user.userId,
    userName: user.name || user.userName || (user.email && user.email.split("@")[0]) || "Unknown",
    avatar: user.avatar || null,
    chatId: user.chatId,
    status: status, // 'EXISTING' or 'NEW'
  })
  
  export const updateChatsAfterNewMessage = (activeChats, availableUsers, newChat) => {
    const updatedActiveChats = [...activeChats, newChat]
    const updatedAvailableUsers = availableUsers.filter((user) => user.id !== newChat.id)
    return { updatedActiveChats, updatedAvailableUsers }
  }
  
  