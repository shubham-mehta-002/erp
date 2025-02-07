package com.client.onboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.client.onboarding.model.Message;
import com.client.onboarding.request.webSocket.SendMessageRequest;
import com.client.onboarding.response.MessageResponse;
import com.client.onboarding.service.ChatService;
import com.client.onboarding.service.MessageService;

@Controller
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:4200"})
public class WebSocketController {

     @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.sendMessage")
public void receiveMessage(@Payload SendMessageRequest chatMessage) {
    System.out.println("Received message via WebSocket: " + chatMessage);

    if (chatMessage == null || chatMessage.getMessageId() == null) {
        System.out.println("Error: Received message is null or missing messageId");
        return;
    }

    Long messageId = chatMessage.getMessageId();
    Message message = messageService.findMessageById(messageId);

    if (message == null) {
        System.out.println("Error: No message found for ID " + messageId);
        return;
    }

    System.out.println("Forwarding message to chat: " + message.getChat().getId());

    MessageResponse response = new MessageResponse(message.getId(), message.getContent(), message.getFilePath(), message.getTimestamp(), message.getSender().getId());
    // Broadcast message to the group
    messagingTemplate.convertAndSend("/group/" + message.getChat().getId(), response);
}
}
