package com.client.onboarding.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.client.onboarding.model.Module;
import com.client.onboarding.model.User;
import com.client.onboarding.repository.ModuleRepository;
import com.client.onboarding.service.UserService;


@RestController 
// @Controller
// @RequestMapping("/api")
public class UserController {
    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private ModuleRepository moduleRepository;
    
    @GetMapping("/api/user/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok(userService.findUserByJwt(jwt));
    }

    
    @GetMapping("api/users")
    public ResponseEntity<List<User>> findConnectedUsers(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwt(jwt);
        if(user == null){
            throw new RuntimeException("User not found");
        }
        return new ResponseEntity<List<User>>(userService.getAllActiveUsers().stream().filter(u -> u.getId() != user.getId()).collect(Collectors.toList()) , HttpStatus.OK);
    }
    
    @PostMapping("/api/opted-modules")
    public ResponseEntity<List<Long>> saveUserSelectedModules(@RequestHeader("Authorization") String jwt, @RequestBody List<Long> optedModules){
        User user = userService.findUserByJwt(jwt);
        if(user == null){
            throw new RuntimeException("User not found");
        }
       
        List<Long> moduleIds = userService.saveUserSelectedModules(user, optedModules);
        return ResponseEntity.ok(moduleIds);
    }
    
    
    @GetMapping("/api/opted-modules")
    public ResponseEntity<List<Module>> getUserSelectedModules(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwt(jwt);
        if(user == null){
            throw new RuntimeException("User not found");
        }
        List<Module> modules = userService.getUserSelectedModules(user);
        return ResponseEntity.ok(modules);
    }
}
