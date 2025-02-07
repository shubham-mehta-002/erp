package com.client.onboarding.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.client.onboarding.model.Message;

@Controller
public class RealTimeChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public RealTimeChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo("/group/public")
    public Message receiveMessage(@Payload Message message) {
        System.out.println(message); 
        simpMessagingTemplate.convertAndSend("/group/" + message.getChat().getId().toString(), message);
        return message;
    }
}
