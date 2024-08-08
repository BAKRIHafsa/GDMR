package com.sqli.gdmr.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Antecedant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAntecedant;
    private LocalDate dateAntecedant;
    private String description;
    @ManyToOne
    @JoinColumn(name = "dossierMedical_id")
    private DossierMedical dossierMedical;
}