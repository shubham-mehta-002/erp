package com.client.onboarding.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.client.onboarding.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // public List<Message> findByChatId(Long chatId);
    
    // @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.timestamp BETWEEN :fromDate AND :toDate ORDER BY m.timestamp ASC")
    // List<Message> findMessagesByChatIdAndDateRange(
    //     @Param("chatId") Long chatId,
    //     @Param("fromDate") LocalDateTime fromDate,
    //     @Param("toDate") LocalDateTime toDate
    // );

    // @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.timestamp BETWEEN :fromDate AND :toDate")
    // List<Message> findMessagesByChatIdAndDateRange(@Param("chatId") Long chatId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.timestamp BETWEEN :fromDate AND :toDate")
    List<Message> findMessagesByChatIdAndDateRange(
        @Param("chatId") Long chatId, 
        @Param("fromDate") LocalDateTime fromDate, 
        @Param("toDate") LocalDateTime toDate
    );
}
