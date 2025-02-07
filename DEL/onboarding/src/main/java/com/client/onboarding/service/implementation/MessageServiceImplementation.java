package com.client.onboarding.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.client.onboarding.exception.ChatException;
import com.client.onboarding.exception.MessageException;
import com.client.onboarding.exception.UserException;
import com.client.onboarding.model.Chat;
import com.client.onboarding.model.Message;
import com.client.onboarding.model.User;
import com.client.onboarding.repository.MessageRepository;
import com.client.onboarding.request.SendMessageRequest;
import com.client.onboarding.response.MessageResponse;
import com.client.onboarding.service.ChatService;
import com.client.onboarding.service.MessageService;
import com.client.onboarding.service.UserService;


@Service
public class MessageServiceImplementation implements MessageService{

    private MessageRepository messageRepository;
    private UserService userService;
    private ChatService chatService;

    public MessageServiceImplementation(MessageRepository messageRepository,UserService userService,ChatService chatService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.chatService = chatService;
    }

    @Override
    public Message findMessageById(Long messageId) throws MessageException {
        Message message = messageRepository.findById(messageId).orElse(null);
        if(message == null){
            throw new MessageException("Message not found with id: " + messageId);
        }
        return message;
    }

    @Override
    public List<MessageResponse> findMessagesByChatId(Long chatId, Long userId, LocalDateTime from, LocalDateTime to) throws ChatException {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new UserException("User not found with id: " + userId);
        }

        Chat chat = chatService.findChatById(chatId);
        if (chat == null) {
            throw new ChatException("Chat not found with id: " + chatId);
        }

        if (!chat.getParticipants().contains(user)) {
            throw new ChatException("User is not a part of this chat");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime fromDateTime = (from == null) ? today.atStartOfDay() : from;
        LocalDateTime toDateTime = (to == null) ? today.atTime(23, 59, 59) : to;

        List<Message> messages = messageRepository.findMessagesByChatIdAndDateRange(chatId, fromDateTime, toDateTime);
        List<MessageResponse> responses = new ArrayList<>();

        for (Message message : messages) {
            MessageResponse response = new MessageResponse();
            response.setId(message.getId());
            response.setContent(message.getContent());
            response.setFilePath(message.getFilePath());
            response.setTimestamp(message.getTimestamp());
            response.setSender(message.getSender().getId());
            responses.add(response);
        }

        return responses;
    }


    @Override
    public MessageResponse sendMessage(SendMessageRequest request) throws UserException, ChatException {
        System.out.println("request : "+request);
        User user = userService.findUserById(request.getUserId());
        if(user == null){
            throw new UserException("User not found with id: " + request.getUserId());
        }
        Chat chat = chatService.findChatById(request.getChatId());
        if(chat == null){
            throw new ChatException("Chat not found with id: " + request.getChatId());
        }
        Message message = new Message();
        message.setChat(chat);
        message.setSender(user);
        message.setContent(request.getMessage());
        message.setTimestamp(LocalDateTime.now());
        message.setFilePath(request.getFilePath() != null ? request.getFilePath() : null);
        messageRepository.save(message);

        MessageResponse response = new MessageResponse(message.getId(), message.getContent(), message.getFilePath(), message.getTimestamp(), message.getSender().getId());
        return response;
    }
    
}
