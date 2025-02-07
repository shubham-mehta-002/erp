package com.client.onboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.client.onboarding.model.User;
import com.client.onboarding.service.UserService;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    @Lazy
    private UserService userService;

    
    @GetMapping
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok(userService.findUserByJwt(jwt));
    }
}
