package com.sqli.gdmr.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Disponibilité {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private LocalTime heuredebut;
    private LocalTime heurefin;
    //private Integer duree;
    @ManyToOne
    private Medecin medecin;
}