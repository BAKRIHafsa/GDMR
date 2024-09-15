package com.sqli.gdmr.DTOs;

import com.sqli.gdmr.Enums.StatusVisite;
import com.sqli.gdmr.Enums.TypesVisite;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DashboardRHDTO {
    @Enumerated(EnumType.STRING)
    private StatusVisite statusVisite;

    @Enumerated(EnumType.STRING)
    private TypesVisite typeVisite;

    private LocalTime heureDebutVisite;
    private LocalTime heureFinVisite;

    private LocalDate date;

    private String nomCollaborateur;
    private String prenomCollaborateur;

    private String nomMedecin;
    private String prenomMedecin;
}
