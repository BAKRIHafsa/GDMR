package com.sqli.gdmr.DTOs;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreneauCreationDTO {
    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime heureDebutVisite;

    @NotNull
    private LocalTime heureFinVisite;

    @NotNull
    private String typeVisite;

    @NotNull
    private Long collaborateurId;

    private LocalDate dateCreation;
}
