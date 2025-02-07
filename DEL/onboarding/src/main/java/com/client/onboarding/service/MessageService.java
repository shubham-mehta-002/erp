package com.client.onboarding.service;

import java.time.LocalDateTime;
import java.util.List;

import com.client.onboarding.exception.ChatException;
import com.client.onboarding.exception.MessageException;
import com.client.onboarding.exception.UserException;
import com.client.onboarding.model.Message;
import com.client.onboarding.request.SendMessageRequest;
import com.client.onboarding.response.MessageResponse;

public interface MessageService {
    public MessageResponse sendMessage(SendMessageRequest request) throws UserException, ChatException;

    public List<MessageResponse> findMessagesByChatId(Long chatId, Long userId,LocalDateTime from, LocalDateTime to) throws ChatException;

    public Message findMessageById(Long messageId) throws MessageException;
}
