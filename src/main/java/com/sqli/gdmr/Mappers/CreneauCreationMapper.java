package com.sqli.gdmr.Mappers;

import com.sqli.gdmr.DTOs.CreneauCreationDTO;
import com.sqli.gdmr.Enums.Role;
import com.sqli.gdmr.Enums.StatusVisite;
import com.sqli.gdmr.Enums.TypesVisite;
import com.sqli.gdmr.Models.Collaborateur;
import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.UserRepository;
import com.sqli.gdmr.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class CreneauCreationMapper {

    @Autowired
    private UserService userService; // Pour récupérer le collaborateur à partir de son ID

    public Creneau toCreneau(CreneauCreationDTO creneauCreationDTO) {
        Creneau creneau = new Creneau();

        // Mapper les attributs du DTO vers l'entité Creneau
        creneau.setDate(creneauCreationDTO.getDate());
        creneau.setHeureDebutVisite(creneauCreationDTO.getHeureDebutVisite());
        creneau.setHeureFinVisite(creneauCreationDTO.getHeureFinVisite());
        creneau.setTypeVisite(TypesVisite.valueOf(creneauCreationDTO.getTypeVisite().toUpperCase()));
        creneau.setDateCreation(creneauCreationDTO.getDateCreation());
        creneau.setStatusVisite(StatusVisite.EN_ATTENTE_VALIDATION);

        // Récupérer et associer le collaborateur à partir de l'ID
        Collaborateur collaborateur = userService.findByIdCollab(creneauCreationDTO.getCollaborateurId());
        creneau.setCollaborateur(collaborateur); // Associer le collaborateur au créneau

        return creneau;
    }
}

