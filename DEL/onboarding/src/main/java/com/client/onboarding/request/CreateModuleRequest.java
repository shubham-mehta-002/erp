package com.client.onboarding.request;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateModuleRequest {
    private String name;
    private List<CreateModuleRequest> subModules = new ArrayList<>();   
}
