package com.client.onboarding.response;

import java.time.LocalDateTime;

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
public class MessageResponse {
    private Long id;
    private String content;
    private String filePath;
    private LocalDateTime timestamp;
    private Long sender;
}
