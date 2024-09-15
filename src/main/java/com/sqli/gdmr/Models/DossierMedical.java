package com.sqli.gdmr.Models;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class DossierMedical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDossierMedical;
    private String description;

    @OneToOne
    private Creneau creneau;


}
