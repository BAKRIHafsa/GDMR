package com.sqli.gdmr.DTOs;

import com.sqli.gdmr.Enums.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    private String nom;
    private String prenom;
    private String username;
    private LocalDate dateNaissance;
    private Role role;
    public UserDTO() {}

    public UserDTO(String nom, String prenom, String username, LocalDate dateNaissance, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.dateNaissance = dateNaissance;
        this.role = role;
    }
}
