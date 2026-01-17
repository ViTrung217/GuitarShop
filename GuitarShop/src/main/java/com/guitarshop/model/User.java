package com.guitarshop.model;
import com.guitarshop.model.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@Data // if using Lombok
public class User {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    private String fullName;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
}
