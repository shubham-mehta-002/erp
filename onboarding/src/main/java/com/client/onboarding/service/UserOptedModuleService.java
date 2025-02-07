package com.client.onboarding.service;

import java.util.List;
// import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.client.onboarding.model.Module;
import com.client.onboarding.model.UserOptedModule;
import com.client.onboarding.repository.ModuleRepository;
import com.client.onboarding.repository.UserOptedModuleRepository;
// import com.client.onboarding.repository.UserRepository;
import com.client.onboarding.request.OptedModuleRequest;
// import com.client.onboarding.response.OptedModuleResponse;

@Service
public class UserOptedModuleService {

    @Autowired
    private UserOptedModuleRepository repository;

    // @Autowired
    // private UserRepository userRepository;

    @Autowired
    private ModuleRepository moduleRepository; 

    public void saveOptedModules(Long userId,  List<OptedModuleRequest> optedModules) {
        List<UserOptedModule> existingOptedModules = repository.findByUserId(userId);
        if(existingOptedModules != null && !existingOptedModules.isEmpty()) {
            throw new RuntimeException("User already opted for modules");
        }
        for (OptedModuleRequest entry : optedModules) {
            Long parentModuleId = entry.getParentModuleId();
            List<Long> subModuleIds = entry.getSubModuleIds();

            Module parentModule = moduleRepository.findById(parentModuleId)
                    .orElseThrow(() -> new RuntimeException("Module not found: " + parentModuleId));

            UserOptedModule parentOpted = new UserOptedModule(userId, parentModule, null);
            repository.save(parentOpted);

            for (Long subModuleId : subModuleIds) {
                Module subModule = moduleRepository.findById(subModuleId)
                        .orElseThrow(() -> new RuntimeException("Submodule not found: " + subModuleId));

                UserOptedModule subModuleOpted = new UserOptedModule(userId, subModule, parentModule);
                repository.save(subModuleOpted);
            }
        }
    }
    // public List<OptedModuleResponse> getOptedModules(Long userId) {
    //     List<UserOptedModule> optedModules = repository.findByUserId(userId);
    //     return optedModules.stream()
    //             .map(module -> new OptedModuleResponse(module.getParentModuleId(), module.getSubModuleIds()))
    //             .collect(Collectors.toList());
    // }
    // public List<OptedModuleResponse> getOptedModules(Long userId) {
    //     List<UserOptedModule> optedModules = repository.findByUserId(userId);
    
    //     return optedModules.stream()
    //             .map(module -> new OptedModuleResponse(
    //                     module.getId(), 
    //                     (), 
    //                     getSubModules(module.getSubModuleIds())
    //             ))
    //             .collect(Collectors.toList());
    // }
    
    // private List<OptedModuleResponse> getSubModules(List<Long> subModuleIds) {
    //     return subModuleRepository.findAllById(subModuleIds).stream()
    //             .map(subModule -> new OptedModuleResponse(
    //                     subModule.getId(),
    //                     subModule.getName(),
    //                     List.of() // Empty list for subModules
    //             ))
    //             .collect(Collectors.toList());
    // }
    
    public List<UserOptedModule> getOptedModules(Long userId) {
        List<UserOptedModule> optedModules = repository.findByUserId(userId);
        System.out.println("optedModules: "+optedModules);
        return optedModules;
    }

}
