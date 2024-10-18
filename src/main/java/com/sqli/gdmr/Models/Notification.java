package com.sqli.gdmr.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private Boolean lu = false;
    private LocalDateTime dateEnvoi;

    @ManyToOne // Lié à Creneau
    private Creneau creneau;

    private int daysBefore;


    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private User destinataire;
}
