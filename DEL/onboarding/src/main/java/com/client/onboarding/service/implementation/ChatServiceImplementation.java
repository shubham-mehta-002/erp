package com.client.onboarding.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.client.onboarding.exception.ChatException;
import com.client.onboarding.exception.UserException;
import com.client.onboarding.model.Chat;
import com.client.onboarding.model.Message;
import com.client.onboarding.model.User;
import com.client.onboarding.repository.ChatRepository;
import com.client.onboarding.request.GroupChatRequest;
import com.client.onboarding.response.ActiveChatsResponse;
import com.client.onboarding.service.ChatService;
import com.client.onboarding.service.UserService;

import jakarta.transaction.Transactional;


@Service
public class ChatServiceImplementation  implements ChatService{

    private ChatRepository chatRepository;
    private UserService userService;

    public ChatServiceImplementation(ChatRepository chatRepository,UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    @Override
    public Chat addUserToGroup(Long userId, Long chatId, Long requestGeneratedByUserId) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if(chat == null){
            throw new ChatException("Chat not found with id: " + chatId);
        }
        if(!chat.isGroupChat()){
            throw new ChatException("Chat is not a group chat");
        }
        if(chat.getParticipants().contains(userId)){
            throw new ChatException("User is already a part of this chat");
        }
        if(!chat.getAdmins().contains(requestGeneratedByUserId)){
            throw new ChatException("Only admins are allowed to add users");
        }

        User user = userService.findUserById(userId);
        if(user == null){
            throw new UserException("User not found with id: " + userId);
        }
        User requestGeneratedByUser = userService.findUserById(requestGeneratedByUserId);
        if(requestGeneratedByUser == null){
            throw new UserException("User not found with id: " + requestGeneratedByUserId);
        }

        chat.getParticipants().add(user);
        chatRepository.save(chat);
        return chat;
    }

    // @Override
    // @Transactional
    // public Chat createChat(Long requestedUserId , Long createdBy,String content) throws UserException {
    //     System.out.println(requestedUserId + " " + createdBy + " " + content);


    //     User user = userService.findUserById(requestedUserId);
    //     System.out.println(user);
    //     if(user == null){
    //         throw new UserException("User not found with id: " + requestedUserId);
    //     }
    //     User createdByUser = userService.findUserById(createdBy);
    //     System.out.println("createdByUser: " + createdByUser);
    //     if(createdByUser == null){
    //         throw new UserException("User not found with id: " + createdBy);
    //     }

    //     Chat isChatAlreadyPresent = chatRepository.findSingleChatByUserIds(user,createdByUser);
    //     System.out.println("isChatAlreadyPresent: " + isChatAlreadyPresent);
    //     if(isChatAlreadyPresent != null){
    //         return isChatAlreadyPresent;
    //     }

    //     if(content == null || content.isEmpty()){
    //         throw new ChatException("Message cannot be empty");
    //     }

    //     Chat chat = new Chat();
    //     // chat.setParticipants(Set.of(user,createdByUser));
    //     chat.setParticipants(new HashSet<>(Arrays.asList(user, createdByUser))); 
    //     chat.setCreatedAt(LocalDateTime.now());
    //     chat.setCreatedBy(createdByUser);
    //     chat.setGroupChat(false);
    //     Chat newChat = chatRepository.save(chat);


    //     System.out.println(newChat);

    //     Message message = new Message();
    //     message.setChat(newChat);
    //     message.setContent(content);
    //     message.setTimestamp(LocalDateTime.now());
    //     message.setSender(createdByUser);
    //     System.out.println("Message" + message);

    //     newChat.getMessages().add(message);
    //     newChat = chatRepository.save(newChat);


    //     System.out.println(newChat);

    //     createdByUser.getChats().add(newChat);
    //     user.getChats().add(newChat);
    //     userService.saveUser(createdByUser);
    //     userService.saveUser(user);

    //     return newChat;
    // }


@Override
@Transactional
public Chat createChat(Long requestedUserId, Long createdBy, String content) throws UserException {
    System.out.println(requestedUserId + " " + createdBy + " " + content);

    User user = userService.findUserById(requestedUserId);
    if (user == null) {
        throw new UserException("User not found with id: " + requestedUserId);
    }
    
    User createdByUser = userService.findUserById(createdBy);
    if (createdByUser == null) {
        throw new UserException("User not found with id: " + createdBy);
    }

    Chat isChatAlreadyPresent = chatRepository.findSingleChatByUserIds(user, createdByUser);
    // System.out.println(isChatAlreadyPresent);
    if (isChatAlreadyPresent != null) {
        // return isChatAlreadyPresent;
        throw new ChatException("Chat already exists between " + user.getName() + " and " + createdByUser.getName());
    }

    if (content == null || content.isEmpty()) {
        throw new ChatException("Message cannot be empty");
    }

    Chat chat = new Chat();
    chat.setParticipants(new HashSet<>(Arrays.asList(user, createdByUser)));
    chat.setCreatedAt(LocalDateTime.now());
    chat.setCreatedBy(createdByUser);
    chat.setGroupChat(false);
    Chat newChat = chatRepository.save(chat);

    Message message = new Message();
    message.setChat(newChat);
    message.setContent(content);
    message.setTimestamp(LocalDateTime.now());
    message.setSender(createdByUser);
    
    System.out.println("Creating message: " + message.getContent());

    newChat.getMessages().add(message); // Add the message to the chat
    newChat = chatRepository.save(newChat);

    createdByUser.getChats().add(newChat);
    user.getChats().add(newChat);
    userService.saveUser(createdByUser);
    userService.saveUser(user);

    return newChat;
}
    @Override
    @Transactional
    public Chat createGroup(GroupChatRequest request, Long userId) throws UserException {
        Set <Long> participants = request.getParticipants();
        Set<User> participantUsers = new HashSet<>();
        for(Long participant : participants){
            User user = userService.findUserById(participant);
            if(user == null){
                throw new UserException("User not found with id: " + participant);
            }
            participantUsers.add(user);
        }

        Chat groupChat = new Chat();
        groupChat.setParticipants(participantUsers);
        groupChat.setCreatedAt(LocalDateTime.now());
        groupChat.setCreatedBy(userService.findUserById(userId));
        groupChat.setGroupChat(true);
        chatRepository.save(groupChat);
        return groupChat;
    }

    @Override
    public List<Chat> findChatByUserId(Long userId) throws UserException {
        User user = userService.findUserById(userId);
        if(user == null){
            throw new UserException("User not found with id: " + userId);
        }
        List<Chat> chats = chatRepository.findChatByUserId(userId);
        return chats;
    }   
    
    @Override
    public Chat findChatById(Long chatId) throws ChatException {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if(chat == null){
            throw new ChatException("Chat not found with id: " + chatId);
        }
        return chat;
    }

    @Override
    public List<ActiveChatsResponse> findActiveChats(Long userId) throws UserException {
        User user = userService.findUserById(userId);
        if(user == null){
            throw new UserException("User not found with id: " + userId);
        }
        List<Chat> activeChats = chatRepository.findActiveChats(user);
        List<ActiveChatsResponse> response = new ArrayList<>();
        for(Chat chat : activeChats){
            ActiveChatsResponse activeChatsResponse = new ActiveChatsResponse();
            activeChatsResponse.setChatId(chat.getId());
            activeChatsResponse.setUserId(chat.getParticipants().stream().filter(u -> !u.getId().equals(userId)).findFirst().get().getId());
            activeChatsResponse.setUserName(chat.getParticipants().stream().filter(u -> !u.getId().equals(userId)).findFirst().get().getName());
            response.add(activeChatsResponse);
        }
        return response;
    }
    
    @Override
    public List<User> findAvailableUsers(Long userId) throws UserException {
        User user = userService.findUserById(userId);
        if(user == null){
            throw new UserException("User not found with id: " + userId);
        }
        List<User> usersAlreadyChattedWith = chatRepository.findSingleChatsDoneByUser(user).stream().map(Chat::getParticipants).flatMap(Set::stream).filter(u -> u.getId() != userId).collect(Collectors.toList());
        List<User> availableUsers = userService.getAllActiveUsers().stream().filter(u -> !usersAlreadyChattedWith.contains(u) && !u.getId().equals(userId)).collect(Collectors.toList());
        return availableUsers;
    }
    @Override
    public List<Long> findChatParticipants(Long chatId) throws ChatException {
        Chat chat = findChatById(chatId);
        if(chat == null){
            throw new ChatException("Chat not found with id: " + chatId);
        }
        return chat.getParticipants().stream().map(User::getId).collect(Collectors.toList());
    }
}
