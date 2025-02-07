package com.client.onboarding.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.client.onboarding.exception.ChatException;
import com.client.onboarding.exception.UserException;
import com.client.onboarding.model.User;
import com.client.onboarding.request.SendMessageRequest;
import com.client.onboarding.response.MessageResponse;
import com.client.onboarding.service.ChatService;
import com.client.onboarding.service.MessageService;
import com.client.onboarding.service.UserService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private MessageService messageService;
    private UserService userService;
    private ChatService chatService;

    public MessageController(MessageService messageService, UserService userService, ChatService chatService) {
        this.messageService = messageService;
        this.userService = userService;
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessageHandler(@RequestHeader("Authorization") String jwt, @RequestBody SendMessageRequest request) throws UserException, ChatException {
        User user = userService.findUserByJwt(jwt);
        request.setUserId(user.getId());
        MessageResponse message = messageService.sendMessage(request);
        return new ResponseEntity<MessageResponse>(message,HttpStatus.OK);
    }
    
    // @GetMapping("/{chatId}")
    // public ResponseEntity<List<MessageResponse>> findMessageByChatIdHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long chatId,@RequestParam(required = false) LocalDate from,@RequestParam(required = false) LocalDate to) throws ChatException, UserException {
    //     User user = userService.findUserByJwt(jwt);
    //     List<MessageResponse> messages = messageService.findMessagesByChatId(chatId,user.getId(),from,to);
    //     return new ResponseEntity<List<MessageResponse>>(messages,HttpStatus.OK);
    // } 
    
    //   @GetMapping("/{chatId}")
    // public ResponseEntity<List<MessageResponse>> getMessages(
    //         @PathVariable Long chatId,
    //         @RequestParam Long userId,
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

    //     List<MessageResponse> messages = messageService.findMessagesByChatId(chatId, userId, from, to);
    //     return ResponseEntity.ok(messages);
    // }


    @GetMapping("/{chatId}")
    public List<MessageResponse> getMessages(
            @PathVariable Long chatId,
            @RequestParam Long userId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Set default values if `from` or `to` is not provided
        LocalDate fromDate = (from != null) ? LocalDate.parse(from, formatter) : LocalDate.now();
        LocalDate toDate = (to != null) ? LocalDate.parse(to, formatter) : LocalDate.now();

        // Convert LocalDate to LocalDateTime
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(23, 59, 59);

        return messageService.findMessagesByChatId(chatId, userId, fromDateTime, toDateTime);
    }
   
}
