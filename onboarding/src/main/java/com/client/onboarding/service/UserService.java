package com.client.onboarding.service;

import java.util.List;

import com.client.onboarding.model.User;
import com.client.onboarding.request.OptedModuleRequest;
// import com.client.onboarding.response.ModuleDTO;


public interface UserService {
    public User findUserById(Long id);
    public User findUserByJwt(String jwt);
    public User findByEmail(String email);
    public User createUser(String email, String password);
}
