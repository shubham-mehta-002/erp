package com.client.onboarding.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.client.onboarding.exception.ChatException;
import com.client.onboarding.exception.UserException;
import com.client.onboarding.model.Chat;
import com.client.onboarding.model.User;
import com.client.onboarding.request.GroupChatRequest;
import com.client.onboarding.request.SingleChatRequest;
import com.client.onboarding.response.ActiveChatsResponse;
import com.client.onboarding.service.ChatService;
import com.client.onboarding.service.UserService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private ChatService chatService;
    private UserService userService;

    public ChatController(ChatService chatService,UserService userService){
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandler(@RequestHeader("Authorization") String jwt, @RequestBody SingleChatRequest request) throws UserException{
        System.out.println(request);
        User user = userService.findUserByJwt(jwt);
        System.out.println(user);
        Chat chat = chatService.createChat(request.getUserId(),user.getId(),request.getContent());
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupChatHandler(@RequestHeader("Authorization") String jwt, @RequestBody GroupChatRequest request) throws UserException{
        User user = userService.findUserByJwt(jwt);
        Chat chat = chatService.createGroup(request,user.getId());
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long chatId) throws ChatException, UserException{
        Chat chat = chatService.findChatById(chatId);
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Chat>> findChatByUserIdHandler(@RequestHeader("Authorization") String jwt) throws ChatException, UserException{
        User user = userService.findUserByJwt(jwt);
        List<Chat> chats = chatService.findChatByUserId(user.getId());
        return new ResponseEntity<List<Chat>>(chats,HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long chatId, @PathVariable Long userId) throws ChatException, UserException{
        User user = userService.findUserByJwt(jwt);
        Chat chat = chatService.addUserToGroup(userId,chatId,user.getId());
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ActiveChatsResponse>> findActiveChatsHandler(@RequestHeader("Authorization") String jwt) throws ChatException, UserException{
        User user = userService.findUserByJwt(jwt);
        List<ActiveChatsResponse> chats = chatService.findActiveChats(user.getId());
        return new ResponseEntity<List<ActiveChatsResponse>>(chats,HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<User>> findAvailableUsersHandler(@RequestHeader("Authorization") String jwt) throws ChatException, UserException{
        User user = userService.findUserByJwt(jwt);
        List<User> users = chatService.findAvailableUsers(user.getId());
        return new ResponseEntity<List<User>>(users,HttpStatus.OK);
    }
}
