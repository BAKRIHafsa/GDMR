package com.sqli.gdmr.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class CreneauCollaborateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creneau_id")
    private Creneau creneau;

    @ManyToOne
    @JoinColumn(name = "collaborateur_id")
    private User collaborateur;

    private LocalDate dateNotification;
    private String statusNotification; // "Pending", "Chosen", "Expired"
    private LocalDate creneauChoiceDate; // Date when the collaborateur made their choice
}
