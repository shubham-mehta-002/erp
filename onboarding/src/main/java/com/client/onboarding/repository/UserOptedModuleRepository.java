package com.client.onboarding.repository;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.client.onboarding.model.UserOptedModule;
import com.client.onboarding.model.Module;

public interface UserOptedModuleRepository extends JpaRepository<UserOptedModule, Long> {
    List<UserOptedModule> findByUserId(Long userId);
    // List<Module> findAllById(Set<Long> moduleIds);
}
