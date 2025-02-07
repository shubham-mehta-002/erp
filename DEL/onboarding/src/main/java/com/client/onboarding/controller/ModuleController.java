package com.client.onboarding.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.client.onboarding.model.Module;
import com.client.onboarding.request.CreateModuleRequest;
import com.client.onboarding.service.ModuleService;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    // @PostMapping("/add")
    // public ResponseEntity<Module> createModule(@RequestBody Module module) {
    //     return ResponseEntity.ok(moduleService.createModule(module));
    // }
    @PostMapping("/add")
    public ResponseEntity<Void> createModule(@RequestBody List<CreateModuleRequest> module) {
        moduleService.createModule(module);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
     public ResponseEntity<List<Module>> getAllParentModules() {
        return ResponseEntity.ok(moduleService.getAllParentModules());
    }
}
