package com.client.onboarding.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString   
@NoArgsConstructor  
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;

    @OneToMany
    private List<UserOptedModule> modules;

    private LocalDateTime createdAt;
    private String status; // ["PENDING" , "APPROVED"]
}




// package com.client.onboarding.model;

// import java.time.LocalDateTime;
// import java.util.Set;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.JoinTable;
// import jakarta.persistence.ManyToMany;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.ToString;

// @Entity
// @Getter
// @Setter
// @ToString   
// @NoArgsConstructor  
// @AllArgsConstructor
// public class User {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String email;
//     private String password;
//     private String name;

//     // @OneToMany
//     // private List<UserOptedModule> modules;
//      @ManyToMany
//     @JoinTable(
//         name = "user_modules",
//         joinColumns = @JoinColumn(name = "user_id"),
//         inverseJoinColumns = @JoinColumn(name = "module_id")
//     )
//     private Set<Module> selectedModules;

//     private LocalDateTime createdAt;
//     private String status; // ["PENDING" , "APPROVED"]
// }
