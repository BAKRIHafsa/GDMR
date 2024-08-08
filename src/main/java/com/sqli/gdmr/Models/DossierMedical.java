package com.sqli.gdmr.Models;

import com.sqli.gdmr.Enums.GroupeSanguin;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class DossierMedical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDossierMedical;
    private String sexe;
    private int height;
    private int weight;
    @Enumerated(EnumType.STRING)
    private GroupeSanguin groupeSanguin;
    private String allergies;
    private String medicalHistory;
    private String medications;
    @OneToMany(mappedBy = "dossierMedical", fetch = FetchType.LAZY)
    private List<Antecedant> antecedants;

    @OneToOne(mappedBy = "dossierMedical")
    private Collaborateur collaborateur;


}