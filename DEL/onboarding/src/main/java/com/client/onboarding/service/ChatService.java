package com.client.onboarding.service;

import java.util.List;

import com.client.onboarding.exception.ChatException;
import com.client.onboarding.exception.UserException;
import com.client.onboarding.model.Chat;
import com.client.onboarding.model.User;
import com.client.onboarding.request.GroupChatRequest;
import com.client.onboarding.response.ActiveChatsResponse;

public interface ChatService {
    public Chat createChat(Long requestedUserId , Long createdBy,String message) throws UserException;
    public List<Chat> findChatByUserId(Long userId) throws UserException;
    public Chat createGroup(GroupChatRequest request, Long userId) throws UserException;
    public Chat addUserToGroup(Long userId, Long chatId, Long requestGeneratedByUserId) throws UserException,ChatException;
    public Chat findChatById(Long chatId) throws ChatException;
    public List<ActiveChatsResponse> findActiveChats(Long userId) throws UserException;
    public List<User> findAvailableUsers(Long userId) throws UserException;
    public List<Long> findChatParticipants(Long chatId) throws ChatException;
}
