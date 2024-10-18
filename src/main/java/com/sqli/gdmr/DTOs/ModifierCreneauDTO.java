package com.sqli.gdmr.DTOs;

import com.sqli.gdmr.Enums.StatusVisite;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ModifierCreneauDTO {
    private LocalDate date;
    private LocalTime heureDebutVisite;
    private LocalTime heureFinVisite;
    private StatusVisite statusVisite;
    private Long medecinId;
}
