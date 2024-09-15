package com.sqli.gdmr.DTOs;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CreneauCreationDTO {
    private LocalDate date;
    private LocalTime heureDebutVisite;
    private LocalTime heureFinVisite;
    private String typeVisite;
    private List<Long> collaborateursIds;
    private LocalDate dateCreation;
}
