package com.sqli.gdmr.Models;

import com.sqli.gdmr.Enums.StatusVisite;
import com.sqli.gdmr.Enums.TypesVisite;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


@Entity
@Data
public class Creneau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCréneau;
    private LocalDate date;
    //private Integer duree;
    private LocalTime heureDebutVisite;
    private LocalTime heureFinVisite;
    //private boolean disponible;// je vais utiliser si le status de visite est validé

    @ManyToOne
    @JoinColumn(name = "charge_rh_id")
    private User chargeRh;

    @ManyToOne
    @JoinColumn(name = "collaborateur_id")
    private Collaborateur collaborateur;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private Medecin medecin;

    @Enumerated(EnumType.STRING)
    private TypesVisite typeVisite;
    private String motif;
    @Enumerated(EnumType.STRING)
    private StatusVisite statusVisite;
    private String justifNonValide;// pour le collaborateur
    private String justifAnnuleMedecin;// si le med n'a pas venu à la visite ou un urgence
    private String justifAnnuleCollaborateur; // si le collaborateur est abscent, un empechement
    private LocalDate dateCreation;


    // Méthode pour calculer la durée si nécessaire
//    public long getDureeMinutes() {
//        return ChronoUnit.MINUTES.between(heureDebutVisite, heureFinVisite);
//    }

}
