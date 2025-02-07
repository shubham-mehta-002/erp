package com.client.onboarding.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.client.onboarding.config.JwtProvider;
import com.client.onboarding.exception.UserException;
import com.client.onboarding.model.Module;
import com.client.onboarding.model.User;
import com.client.onboarding.model.enums.ChatStatus;
import com.client.onboarding.model.enums.Status;
import com.client.onboarding.repository.ModuleRepository;
import com.client.onboarding.repository.UserRepository;
import com.client.onboarding.response.OptedModuleResponse;
import com.client.onboarding.service.UserService;

@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository; 
    private JwtProvider jwtProvider;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private ModuleRepository moduleRepository;

    public UserServiceImplementation(UserRepository userRepository, JwtProvider jwtProvider, @Lazy UserService userService,PasswordEncoder passwordEncoder,ModuleRepository moduleRepository) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public User findUserById(Long id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        throw new UserException("User not found with id: " + id);
    }

    @Override
    public User findUserByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        
        if(user == null){
            throw new UserException("User not found with email: " + email);
        }
        return user;
    }
    @Override
    public User findByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public User createUser(String email, String password) {
        if(userRepository.findByEmail(email) != null){
            throw new UserException("User already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllChatActiveUsers(ChatStatus status) {
        return userRepository.findAllByChatStatus(status); 
    }

    @Override
    public List<User> getAllActiveUsers() {
        return userRepository.findAllByStatus(Status.ACTIVE);
    }
    @Override
    public List<Long> saveUserSelectedModules(User user, List<Long> selectedModuleIds) {
        List<Module> selectedModules = new ArrayList<>();
        for(Long moduleId : selectedModuleIds){
            Module module = moduleRepository.findById(moduleId).orElseThrow(
                () -> new RuntimeException("Module not found with id: " + moduleId)
            );
            selectedModules.add(module);
        }

        user.setModules(selectedModules);
        userRepository.save(user);
        return selectedModuleIds;
        
    }

    @Override
    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Override
    public List<Module> getUserSelectedModules(User user) {
        List<Module> selectedModules = user.getModules();
        List<Long> selectedModuleIds = selectedModules.stream().map(Module::getId).collect(Collectors.toList());
        // List<Module> modules = moduleRepository.findAllById(selectedModuleIds);
        // return formatUserModuleResponse(selectedModuleIds);
        return formatUserModuleResponse(selectedModuleIds);
        
    }

    private List<Module> formatUserModuleResponse(List<Long> selectedModuleIds){
        // Map to store module and its response data
        HashMap<Long, OptedModuleResponse> moduleMap = new HashMap<>();

        // Process each selected module ID
        for (Long moduleId : selectedModuleIds) {
            Module selectedModule = moduleRepository.findById(moduleId).orElseThrow(
                () -> new RuntimeException("Module not found with id: " + moduleId)
            );
            
            // Create response for selected module with its children
            OptedModuleResponse selectedOptedModule = new OptedModuleResponse();
            selectedOptedModule.setId(selectedModule.getId());
            selectedOptedModule.setName(selectedModule.getName());
            selectedOptedModule.setSubModules(selectedModule.getSubModules());
            
            // Build parent chain
            Module currentModule = selectedModule;
            Module lastChild = selectedModule;
            
            while (currentModule.getParentModule() != null) {
                Module parentModule = currentModule.getParentModule();
                Long parentId = parentModule.getId();
                
                // Create or get parent module response
                OptedModuleResponse parentOptedModule;
                if (!moduleMap.containsKey(parentId)) {
                    parentOptedModule = new OptedModuleResponse();
                    parentOptedModule.setId(parentId);
                    parentOptedModule.setName(parentModule.getName());
                    parentOptedModule.setSubModules(new ArrayList<>());
                    moduleMap.put(parentId, parentOptedModule);
                } else {
                    parentOptedModule = moduleMap.get(parentId);
                }
                
                // Create a clean child module (without siblings)
                Module cleanChild = new Module();
                cleanChild.setId(lastChild.getId());
                cleanChild.setName(lastChild.getName());
                cleanChild.setSubModules(lastChild.getSubModules());
                
                // Add only this child to parent's subModules
                List<Module> parentSubModules = parentOptedModule.getSubModules();
                if (!containsModule(parentSubModules, cleanChild.getId())) {
                    parentSubModules.add(cleanChild);
                    moduleMap.remove(cleanChild.getId()); // deldeldleldeldel
                }
                
                // Update for next iteration
                lastChild = new Module();
                lastChild.setId(parentModule.getId());
                lastChild.setName(parentModule.getName());
                lastChild.setSubModules(parentOptedModule.getSubModules());
                
                currentModule = parentModule;
            }
            
            // If this is a root module or hasn't been added to any parent
            if (currentModule.getParentModule() == null && !moduleMap.containsKey(currentModule.getId())) {
                moduleMap.put(currentModule.getId(), createOptedModuleResponse(currentModule));
            }
        }

        // Convert to final response list
        List<Module> responseModules = new ArrayList<>();
        for (OptedModuleResponse optedModule : moduleMap.values()) {
            responseModules.add(mapToModule(optedModule));
        }

        return responseModules;
    }

    private boolean containsModule(List<Module> modules, Long moduleId) {
        return modules.stream().anyMatch(m -> m.getId().equals(moduleId));
    }

    private OptedModuleResponse createOptedModuleResponse(Module module) {
        OptedModuleResponse response = new OptedModuleResponse();
        response.setId(module.getId());
        response.setName(module.getName());
        response.setSubModules(module.getSubModules());
        return response;
    }

    private Module mapToModule(OptedModuleResponse optedModuleResponse) {
        Module module = new Module();
        module.setId(optedModuleResponse.getId());
        module.setName(optedModuleResponse.getName());
        module.setSubModules(optedModuleResponse.getSubModules());
        return module;
    }
        
}
