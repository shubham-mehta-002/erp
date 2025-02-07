package com.client.onboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.client.onboarding.model.Chat;
import com.client.onboarding.model.User;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.isGroupChat = false AND :user1 Member of c.participants AND :user2 Member of c.participants")
    public Chat findSingleChatByUserIds(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT c FROM Chat c join c.participants p WHERE p.id = :userId")
    public List<Chat> findChatByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Chat c WHERE :user Member of c.participants")
    public List<Chat> findActiveChats(@Param("user") User user);

    @Query("SELECT c FROM Chat c WHERE c.isGroupChat = false AND :user Member of c.participants")
    public List<Chat> findSingleChatsDoneByUser(@Param("user") User user);
}
