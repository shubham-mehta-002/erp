package com.client.onboarding.model;

import java.time.LocalDateTime;
import java.util.List;

import com.client.onboarding.model.enums.ChatStatus;
import com.client.onboarding.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor  
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;

    @ManyToMany
    @JoinTable(
        name = "user_modules",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "module_id")
    )

    @JsonIgnore
    private List<Module> modules;

    private LocalDateTime createdAt;
    private Status status; // ["ACTIVE" , "INACTIVE"]
    private ChatStatus chatStatus;

    @ManyToMany
    @JoinTable(
        name = "user_chats",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    @JsonIgnore
    private List<Chat> chats;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", status=" + status +
                ", chatStatus=" + chatStatus +
                '}';
    }
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
