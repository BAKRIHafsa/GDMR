package com.sqli.gdmr.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Data
public class Creneau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCréneau;
    private LocalDateTime date;
    private int duration;
    private boolean disponibilite;

    private LocalTime heureDebutSession;

    private LocalTime heureFinSession;

    @ManyToOne
    private ChargeRH chargeRH;

    @ManyToOne
    @JoinColumn(name = "MedecinId")
    private Medecin medecin;

}
