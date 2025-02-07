package com.client.onboarding.service;

import java.util.List;

import com.client.onboarding.model.Module;
import com.client.onboarding.model.User;
import com.client.onboarding.model.enums.ChatStatus;


public interface UserService {
    public User findUserById(Long id);
    public User findUserByJwt(String jwt);
    public User findByEmail(String email);
    public User createUser(String email, String password);
    public List<Long> saveUserSelectedModules(User user, List<Long> selectedModuleIds);
    public List<Module> getUserSelectedModules(User user);
    public User saveUser(User user);
    public List<User> getAllChatActiveUsers(ChatStatus status);
    public List<User> getAllActiveUsers();
}
