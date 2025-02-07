package com.client.onboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.client.onboarding.model.User;
import com.client.onboarding.model.enums.ChatStatus;
import com.client.onboarding.model.enums.Status;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
    public List<User> findAllByStatus(Status status);
    public List<User> findAllByChatStatus(ChatStatus status);
    List<User> findAllByIdNotAndChatsIsNotEmpty(Long userId);
    List<User> findAllByIdNotAndChatsIsEmpty(Long userId);
    
}
