package com.client.onboarding.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SendMessageRequest {
    private Long userId; // The user who is sending the message
    private Long chatId; // The chat where the message is sent
    private String message; // The content of the message
    private String filePath; // The file path of the message
}
