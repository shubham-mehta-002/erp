package com.client.onboarding.service;

import com.client.onboarding.model.Module;
import com.client.onboarding.repository.ModuleRepository;
import com.client.onboarding.request.CreateModuleRequest;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    // public Module createModule(Module module) {
    //     if (module.getSubModules() != null) {
    //         module.getSubModules().forEach(subModule -> subModule.setParentModule(module));
    //     }
    //     return moduleRepository.save(module);
    // }
       // Main method to create modules

    public void createModule(List<CreateModuleRequest> request) {
        for (CreateModuleRequest moduleRequest : request) {
            // save level 1 root module
            Module rootModule = saveModule(moduleRequest, null);
        }
        return;
    }
    
    // save subModules using recursive approach
    private Module saveModule(CreateModuleRequest moduleRequest, Module parentModule) {
        Module module = new Module();
        module.setName(moduleRequest.getName());
        module.setParentModule(parentModule);

        Module savedModule = moduleRepository.save(module);
        for (CreateModuleRequest subModuleRequest : moduleRequest.getSubModules()) {
            saveModule(subModuleRequest, savedModule);  
        }
        return savedModule;
    }

    public List<Module> getAllParentModules() {
        return moduleRepository.findByParentModuleIsNull();
    }
}







