package com.client.onboarding.response;

import java.util.List;
import com.client.onboarding.model.Module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OptedModuleResponse {
    private Long id;
    private String name;
    // private List<OptedModuleResponse> subModules;
    private List<Module> subModules;

}
