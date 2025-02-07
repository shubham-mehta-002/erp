package com.client.onboarding.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.client.onboarding.service.PasswordResetService;

@RestController
@RequestMapping
public class SetPasswordController {

    private final PasswordResetService passwordResetService;

    public SetPasswordController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/request/erp")
    public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String,String> erpRequest) {
        String email = erpRequest.get("email");
        System.out.println("email: "+email);
        passwordResetService.generateAndSendToken(email);
        return ResponseEntity.ok("Set password email sent.");
    }

    @PostMapping("/set-password")
    public ResponseEntity<String> verifyToken(@RequestBody Map<String,String> setPasswordRequest) {
        String token = setPasswordRequest.get("token");
        String email = setPasswordRequest.get("email");
        String password = setPasswordRequest.get("password");
        boolean isValid = passwordResetService.verifyToken(token,email,password);
        return isValid ? ResponseEntity.ok("Token is valid.") : ResponseEntity.badRequest().body("Invalid or expired token.");
    }
}
