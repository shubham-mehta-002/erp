// package com.client.onboarding.model;
// import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.ToString;

// @Entity
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @ToString
// @Table(name = "user_opted_modules")
// public class UserOptedModule {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private Long userId; 

//     @ManyToOne
//     @JoinColumn(name = "module_id") 
//     private Module module;

//     @ManyToOne
//     @JoinColumn(name = "parent_module_id") 
//     private Module parentModule;

//     public UserOptedModule(Long userId, Module module, Module parentModule) {
//         this.userId = userId;
//         this.module = module;
//         this.parentModule = parentModule;
//     }

// }
