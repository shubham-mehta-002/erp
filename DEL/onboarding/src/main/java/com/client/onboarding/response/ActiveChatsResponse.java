package com.client.onboarding.response;

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
public class ActiveChatsResponse {
    private Long chatId;
    private Long userId;
    private String userName;
}
