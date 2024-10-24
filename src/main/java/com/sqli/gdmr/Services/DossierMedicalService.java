package com.sqli.gdmr.Services;

import com.sqli.gdmr.Enums.StatusVisite;
import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Models.DossierMedical;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.CreneauRepository;
import com.sqli.gdmr.Repositories.DossierMedicalRepository;
import com.sqli.gdmr.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DossierMedicalService {

    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;
    @Autowired
    private CreneauRepository creneauRepository;

    @Autowired
    private UserRepository userRepository;

    public DossierMedical ajouterDossierMedical(DossierMedical dossierMedical, Long idCollaborateur) {
        // Get the Creneau associated with the DossierMedical
        Creneau creneau = creneauRepository.findById(dossierMedical.getCreneau().getIdCréneau())
                .orElseThrow(() -> new RuntimeException("Creneau not found")); // Handle not found

        // Check the status of the visit
        if (creneau.getStatusVisite() == StatusVisite.EN_COURS || creneau.getStatusVisite() == StatusVisite.TERMINE) {
            // Set the Creneau in the DossierMedical before saving
            dossierMedical.setCreneau(creneau);

            // Retrieve the Collaborateur by ID and set it in DossierMedical
            User collaborateur = userRepository.findById(idCollaborateur)
                    .orElseThrow(() -> new RuntimeException("Collaborateur not found")); // Handle not found
            dossierMedical.setCollaborateur(collaborateur); // Set the User as the collaborator

            return dossierMedicalRepository.save(dossierMedical);
        } else {
            throw new IllegalArgumentException("Cannot add DossierMedical: Visit must be EN_COURS or TERMINE.");
        }
    }



    public DossierMedical obtenirDossierParCreneau(Long idCreneau) {
        return dossierMedicalRepository.findByCreneau_IdCréneau(idCreneau);
    }

    public List<DossierMedical> obtenirDossiersParCollaborateur(Long idCollaborateur) {
        return dossierMedicalRepository.findByCollaborateur_idUser(idCollaborateur);
    }

    public DossierMedical modifierDossierMedical(Long id, DossierMedical dossierMedicalDetails) {
        // Retrieve the dossier medical by ID
        DossierMedical dossierMedical = dossierMedicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dossier médical non trouvé avec l'ID: " + id));

        // Update the fields
        dossierMedical.setDescription(dossierMedicalDetails.getDescription());
        dossierMedical.setMedicaments(dossierMedicalDetails.getMedicaments());

        // Save the updated dossier and return it
        return dossierMedicalRepository.save(dossierMedical);
    }
}
