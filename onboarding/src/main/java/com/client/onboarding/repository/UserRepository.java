package com.client.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.client.onboarding.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}
