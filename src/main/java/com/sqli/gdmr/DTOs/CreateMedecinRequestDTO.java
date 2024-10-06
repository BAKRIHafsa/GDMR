package com.sqli.gdmr.DTOs;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateMedecinRequestDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String username;
    private LocalDate dateNaissance;
    private String experience;
    private String qualification;
    private String siteTravail;
    private String role;
}
