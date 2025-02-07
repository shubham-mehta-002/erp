package com.client.onboarding.request;

import java.util.HashSet;
import java.util.Set;

import com.client.onboarding.model.User;

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
public class GroupChatRequest {
    private Set<Long> participants = new HashSet<>();
    private String groupName;
    // private String description;
}
