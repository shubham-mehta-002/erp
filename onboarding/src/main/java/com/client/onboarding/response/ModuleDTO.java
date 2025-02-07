// package com.client.onboarding.response;

// import lombok.*;

// import java.util.List;
// import java.util.Set;
// import java.util.stream.Collectors;
// import com.client.onboarding.model.Module;

// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// public class ModuleDTO {
//     private Long id;
//     private String name;
//     private List<ModuleDTO> subModules;

//     // Convert Module to DTO (with selected sub-modules only)
//     public static ModuleDTO fromEntity(Module module, Set<Long> selectedModuleIds) {
//         List<ModuleDTO> subModules = module.getSubModules().stream()
//                 .filter(subModule -> selectedModuleIds.contains(subModule.getId())) // Only keep explicitly selected sub-modules
//                 .map(subModule -> ModuleDTO.fromEntity(subModule, selectedModuleIds))
//                 .collect(Collectors.toList());

//         return new ModuleDTO(module.getId(), module.getName(), subModules);
//     }
// }
