package com.sqli.gdmr.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MEDECIN")
@Data
public class Medecin extends User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long IdMedecin;
    private String siteTravail;
    private String specialite;
    private String qualification;
    private String experience;


}
