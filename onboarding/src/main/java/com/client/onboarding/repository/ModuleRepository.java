package com.client.onboarding.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.client.onboarding.model.Module;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByParentModuleIsNull();
    Optional<Module> findById(Long id);
}
