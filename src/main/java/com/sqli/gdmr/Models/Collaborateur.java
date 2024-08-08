package com.sqli.gdmr.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("COLLABORATEUR")
@Data
public class Collaborateur extends User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long IdCollaborateur;
    private String departement;
    @OneToOne
    @JoinColumn(name = "dossierMedical_Id")
    private DossierMedical dossierMedical;
    @OneToMany(mappedBy = "collaborateur", fetch = FetchType.LAZY)
    private List<Visite> visites=new ArrayList<>();

}
