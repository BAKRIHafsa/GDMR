package com.sqli.gdmr.Models;

import com.sqli.gdmr.Enums.Role;
import com.sqli.gdmr.Enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    @Column(unique = true)
    private String username;
    private String password;
    private String nom;
    private String prenom;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private UserStatus Status = UserStatus.CREATED;
    private LocalDate dateNaissance;



}
