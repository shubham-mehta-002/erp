// package com.client.onboarding.controller;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.client.onboarding.request.OptedModuleRequest;
// import com.client.onboarding.service.UserService;

// @RestController
// @RequestMapping("/api/del-opted-modules")
// public class DelOptedModules {

//     @Autowired
//     private UserService userService;


//     @PostMapping("/{userId}/modules")
//     public ResponseEntity<String> saveUserModules(@PathVariable Long userId, @RequestBody List<OptedModuleRequest> selectedModules) {
//         userService.saveUserSelectedModules(userId, selectedModules);
//         return ResponseEntity.ok("Modules assigned successfully!");
//     }
// }
