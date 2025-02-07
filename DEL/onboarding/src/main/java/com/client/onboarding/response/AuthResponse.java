package com.client.onboarding.response;

import com.client.onboarding.model.User;

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
public class AuthResponse {
    public AuthResponse(String jwt, String message) {
        this.jwt = jwt;
        this.message = message;
        this.success = true;
    }
    private String jwt;
    private User user;
    private String message; 
    private boolean success;
}
