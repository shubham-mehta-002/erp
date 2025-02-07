package com.client.onboarding.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptedModuleResponse {
    private Long id;
    private String name;
    private List<OptedModuleResponse> subModules;
}
