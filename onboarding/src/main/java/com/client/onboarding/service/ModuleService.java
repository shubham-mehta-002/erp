package com.client.onboarding.service;

import com.client.onboarding.model.Module;
import com.client.onboarding.repository.ModuleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public Module createModule(Module module) {
        if (module.getSubModules() != null) {
            module.getSubModules().forEach(subModule -> subModule.setParentModule(module));
        }
        return moduleRepository.save(module);
    }

    public List<Module> getAllParentModules() {
        return moduleRepository.findByParentModuleIsNull();
    }
}
