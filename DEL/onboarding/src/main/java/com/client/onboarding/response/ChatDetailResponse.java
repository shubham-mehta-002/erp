package com.client.onboarding.response;

import java.util.List;

import com.client.onboarding.model.Message;

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
public class ChatDetailResponse {
    private List<Message> messages;
    private List<Long> userIds;
}
