package com.sqli.gdmr.Mappers;

import com.sqli.gdmr.DTOs.CreneauCreationDTO;
import com.sqli.gdmr.Enums.TypesVisite;
import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Models.CreneauCollaborateur;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class CreneauCreationMapper {

    @Autowired
    private UserRepository userRepository;

    public static Creneau toCreneau(CreneauCreationDTO dto) {
        Creneau creneau = new Creneau();
        creneau.setDate(dto.getDate());
        creneau.setHeureDebutVisite(dto.getHeureDebutVisite());
        creneau.setHeureFinVisite(dto.getHeureFinVisite());
        TypesVisite typeVisite = TypesVisite.valueOf(dto.getTypeVisite());
        creneau.setTypeVisite(typeVisite);
        creneau.setDateCreation(dto.getDateCreation());
        return creneau;
    }

    public List<CreneauCollaborateur> toCreneauCollaborateurs(Creneau creneau, List<Long> collaborateursIds) {
        List<CreneauCollaborateur> creneauCollaborateurs = new ArrayList<>();
        for (Long collaborateurId : collaborateursIds) {
            User collaborateur = userRepository.findById(collaborateurId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Collaborateur ID: " + collaborateurId));

            CreneauCollaborateur creneauCollaborateur = new CreneauCollaborateur();
            creneauCollaborateur.setCreneau(creneau);
            creneauCollaborateur.setCollaborateur(collaborateur);

            creneauCollaborateurs.add(creneauCollaborateur);
        }
        return creneauCollaborateurs;
    }
}

