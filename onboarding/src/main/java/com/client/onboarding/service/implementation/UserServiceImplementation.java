package com.client.onboarding.service.implementation;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.client.onboarding.config.JwtProvider;
import com.client.onboarding.exception.UserException;
import com.client.onboarding.model.User;
import com.client.onboarding.repository.UserRepository;
import com.client.onboarding.service.UserService;

@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository; 
    private JwtProvider jwtProvider;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UserRepository userRepository, JwtProvider jwtProvider, @Lazy UserService userService,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserById(Long id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        throw new UserException("User not found with id: " + id);
    }

    @Override
    public User findUserByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        
        if(user == null){
            throw new UserException("User not found with email: " + email);
        }
        return user;
    }
    @Override
    public User findByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public User createUser(String email, String password) {
        if(userRepository.findByEmail(email) != null){
            throw new UserException("User already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    
}
