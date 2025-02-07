package com.client.onboarding.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.client.onboarding.model.User;
import com.client.onboarding.request.OptedModuleRequest;

@Service
public class UserOptedModuleService {

    @Autowired
    private UserService userService;


    public void saveOptedModules(@RequestHeader("Authorization") String jwt, List<OptedModuleRequest> optedModules){
        User user = userService.findUserByJwt(jwt);
        if(user == null) {
            throw new RuntimeException("Invalid JWT");
        }

        for(OptedModuleRequest optedModule : optedModules){
            Long parentId = optedModule.getParentModuleId();
            List<Long> childIds = optedModule.getSubModuleIds();
            
        }
        
    }

}
