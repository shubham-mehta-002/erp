package com.client.onboarding.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.client.onboarding.model.User;
// import com.client.onboarding.model.UserOptedModule;
import com.client.onboarding.request.OptedModuleRequest;
import com.client.onboarding.response.OptedModuleResponse;
import com.client.onboarding.service.UserOptedModuleService;
import com.client.onboarding.service.UserService;

@RestController
@RequestMapping("/api/opted-modules")
public class UserOptedModuleController {

    @Autowired
    private UserOptedModuleService service;

    @Autowired
    private UserService userService ;

//     @PostMapping
//     public ResponseEntity<String> saveOptedModules(@RequestHeader("Authorization") String jwt,
//                                                    @RequestBody List<OptedModuleRequest> request) {
//         // System.out.println("request: "+request);
//         User user = userService.findUserByJwt(jwt);
//         if(user == null) {
//             return ResponseEntity.badRequest().body("Invalid JWT");
//         }
//         service.saveOptedModules(user.getId(),request);
//         // System.out.println("Modules saved successfully!");
//         return ResponseEntity.ok("Modules saved successfully!");
//     }

//     @GetMapping
//     public ResponseEntity<List<OptedModuleResponse>> getOptedModules(@RequestHeader("Authorization") String jwt) {
//         User user = userService.findUserByJwt(jwt);
//         List<UserOptedModule> optedModules = service.getOptedModules(user.getId());
    
//         List<OptedModuleResponse> formattedResponse = formatResponse(optedModules);
        
//         return ResponseEntity.ok(formattedResponse);
//     }


//     private List<OptedModuleResponse> formatResponse(List<UserOptedModule> optedModules) {
//     Map<Long, OptedModuleResponse> moduleMap = new HashMap<>();
    
//     for (UserOptedModule optedModule : optedModules) {
//         Long moduleId = optedModule.getModule().getId();
//         String moduleName = optedModule.getModule().getName();

//         moduleMap.put(moduleId, new OptedModuleResponse(moduleId, moduleName, new ArrayList<>()));
//     }

//     List<OptedModuleResponse> rootModules = new ArrayList<>();
    
//     for (UserOptedModule optedModule : optedModules) {
//         Long moduleId = optedModule.getModule().getId();
//         OptedModuleResponse moduleResponse = moduleMap.get(moduleId);

//         if (optedModule.getParentModule() != null) {
//             Long parentId = optedModule.getParentModule().getId();
//             OptedModuleResponse parentResponse = moduleMap.get(parentId);
//             if (parentResponse != null) {
//                 parentResponse.getSubModules().add(moduleResponse);
//             }
//         } else {
//             rootModules.add(moduleResponse);
//         }
//     }

//     return rootModules;
// }

    
}
