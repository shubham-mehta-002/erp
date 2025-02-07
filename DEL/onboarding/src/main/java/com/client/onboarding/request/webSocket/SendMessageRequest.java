package com.client.onboarding.request.webSocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SendMessageRequest {
    // private Long chatId;
    // private String message;
    // private String senderId;
    private Long messageId;
}
