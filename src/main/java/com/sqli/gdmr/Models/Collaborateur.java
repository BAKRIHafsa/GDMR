package com.sqli.gdmr.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne(mappedBy = "collaborateur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Antecedant antecedant;


}
