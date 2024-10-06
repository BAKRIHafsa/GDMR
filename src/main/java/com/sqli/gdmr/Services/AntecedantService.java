package com.sqli.gdmr.Services;

import com.sqli.gdmr.Models.Antecedant;
import com.sqli.gdmr.Models.Collaborateur;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.AntecedantRepository;
import com.sqli.gdmr.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntecedantService {
    @Autowired
    private AntecedantRepository antecedantRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public Antecedant getAntecedantForCurrentUser() {
        User currentUser = userService.getCurrentUser(); // Récupérer l'utilisateur connecté
        if (currentUser != null && currentUser instanceof Collaborateur) {
            Collaborateur currentCollaborateur = (Collaborateur) currentUser; // Récupérer le collaborateur
            return antecedantRepository.findByCollaborateur(currentCollaborateur)
                    .orElseThrow(() -> new RuntimeException("Antecedant not found for the current collaborator"));
        }
        throw new RuntimeException("Current user is not associated with any collaborator");
    }




    public Antecedant saveAntecedant(Antecedant antecedant) {
        User currentUser = userService.getCurrentUser();

        Collaborateur collaborateur = userRepository.findById(currentUser.getIdUser())
                .filter(user -> user instanceof Collaborateur)
                .map(user -> (Collaborateur) user)
                .orElseThrow(() -> new RuntimeException("Collaborateur not found for user id: " + currentUser.getIdUser()));

        antecedant.setCollaborateur(collaborateur);

        return antecedantRepository.save(antecedant);
    }
    public Antecedant updateAntecedant(Long antecedantId, Antecedant antecedant) {
        User currentUser = userService.getCurrentUser();

        // Rechercher l'antécédent existant
        Antecedant existingAntecedant = antecedantRepository.findById(antecedantId)
                .orElseThrow(() -> new RuntimeException("Antecedant not found"));

        // Vérifier que l'utilisateur connecté est le collaborateur associé à cet antécédent
        Collaborateur currentCollaborateur = (Collaborateur) currentUser; // Assurez-vous que l'utilisateur est un collaborateur
        if (!existingAntecedant.getCollaborateur().equals(currentCollaborateur)) {
            throw new RuntimeException("You are not authorized to update this antecedent");
        }

        // Mettez à jour les détails de l'antécédent
        existingAntecedant.setSexe(antecedant.getSexe());
        existingAntecedant.setHeight(antecedant.getHeight());
        existingAntecedant.setWeight(antecedant.getWeight());
        existingAntecedant.setGroupeSanguin(antecedant.getGroupeSanguin());
        existingAntecedant.setAllergies(antecedant.getAllergies());
        existingAntecedant.setMedicaments(antecedant.getMedicaments());
        existingAntecedant.setFume(antecedant.getFume());

        // Enregistrez les modifications
        return antecedantRepository.save(existingAntecedant);
    }


}
