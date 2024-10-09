package com.sqli.gdmr.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomFichier;
    private String typeFichier;
    private Long tailleFichier;
    private String cheminFichier;

    @ManyToOne
    @JoinColumn(name = "creneau_id")
    private Creneau creneau;
}
