package com.sqli.gdmr.DTOs;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
