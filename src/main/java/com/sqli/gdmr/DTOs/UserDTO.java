package com.sqli.gdmr.DTOs;

import lombok.Data;

@Data
public class UserDTO {
    private String nom;
    private String prenom;
    public UserDTO() {}

    public UserDTO(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }
}
