package com.sqli.gdmr.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sqli.gdmr.Enums.GroupeSanguin;
import com.sqli.gdmr.Enums.Sexe;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Antecedant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAntecedant;
    @Enumerated(EnumType.STRING)
    private Sexe sexe;
    private Double height;
    private Double weight;
    @Enumerated(EnumType.STRING)
    private GroupeSanguin groupeSanguin;
    private String allergies;
    private String medicaments;
    private Boolean fume;

    @OneToOne
    @JoinColumn(name = "collaborateur_id")
    @JsonIgnore
    private Collaborateur collaborateur;

    @Override
    public String toString() {
        return "Antecedant{" +
                "idAntecedant=" + idAntecedant +
                ", sexe=" + sexe +
                ", height=" + height +
                ", weight=" + weight +
                ", groupeSanguin=" + groupeSanguin +
                ", allergies='" + allergies + '\'' +
                ", medicaments='" + medicaments + '\'' +
                ", fume=" + fume +
                '}';
    }


}



