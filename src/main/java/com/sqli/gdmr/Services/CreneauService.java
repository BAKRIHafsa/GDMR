package com.sqli.gdmr.Services;

import com.sqli.gdmr.DTOs.CreneauCreationDTO;
import com.sqli.gdmr.Enums.Role;
import com.sqli.gdmr.Enums.StatusVisite;
import com.sqli.gdmr.Mappers.CreneauCreationMapper;
import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Models.CreneauCollaborateur;
import com.sqli.gdmr.Models.Notification;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.CreneauCollaborateurRepository;
import com.sqli.gdmr.Repositories.CreneauRepository;
import com.sqli.gdmr.Repositories.NotificationRepository;
import com.sqli.gdmr.Repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CreneauService {

    @Autowired
    private CreneauRepository creneauRepository;

    @Autowired
    private DisponibilitéService disponibilitéService;

    @Autowired
    private CreneauCollaborateurRepository creneauCollaborateurRepository;

    @Autowired
    private CreneauCreationMapper creneauCreationMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;


//    public String creerCreneauParChargeRh(Creneau creneau) {
//        try {
//            // Vérifier si le créneau est disponible
//            boolean isAvailable = disponibilitéService.isCreneauAvailable(creneau.getDate(), creneau.getHeureDebutVisite(), creneau.getHeureFinVisite());
//
//            if (!isAvailable) {
//                return "Le créneau sélectionné n'est plus disponible.";
//            }
//
//            User currentUser = userService.getCurrentUser();
//
//            creneau.setChargeRh(currentUser);
//
//            creneau.setStatusVisite(StatusVisite.EN_ATTENTE_VALIDATION);
//
//            creneauRepository.save(creneau);
//
//            return "Créneau créé avec succès par le Chargé RH.";
//        } catch (Exception e) {
//            return "Erreur lors de la création du créneau : " + e.getMessage();
//        }
//    }
//@Transactional
//public void createCreneauAndNotify(CreneauCreationDTO creneauCreationDTO) {
//    // Step 1: Map DTO to Creneau entity
//    Creneau creneau = creneauCreationMapper.toCreneau(creneauCreationDTO);
//    creneau.setDateCreation(LocalDate.now()); // Set the creation date
//
//    // Step 2: Save the Creneau entity
//    creneau = creneauRepository.save(creneau);
//
//    // Step 3: Map the CreneauCreationDTO to CreneauCollaborateur entities
//    List<CreneauCollaborateur> creneauCollaborateurs = creneauCreationMapper.toCreneauCollaborateurs(creneau, creneauCreationDTO.getCollaborateursIds());
//
//    // Step 4: Save all CreneauCollaborateur entities
//    creneauCollaborateurRepository.saveAll(creneauCollaborateurs);
//
//    // Step 5: Send notifications to all collaborators
//    notificationService.sendNotificationsToCollaborateurs(creneauCollaborateurs);
//}
public void creerCreneauEtEnvoyerNotifications(CreneauCreationDTO creneauDTO) {
    // Utiliser le mapper pour créer un objet Creneau
    Creneau creneau = creneauCreationMapper.toCreneau(creneauDTO);
    creneau.setDateCreation(LocalDate.now());

    creneau.setStatusVisite(StatusVisite.EN_ATTENTE_VALIDATION);

    // Vérifier si le créneau est disponible pour au moins un médecin
    boolean isAvailable = disponibilitéService.isCreneauAvailable(creneau.getDate(), creneau.getHeureDebutVisite(), creneau.getHeureFinVisite());

    if (!isAvailable) {
        // Gérer le cas où aucun médecin n'est disponible
        throw new IllegalArgumentException("Le créneau sélectionné n'est plus disponible chez aucun médecin.");
    }

    // Sauvegarder le créneau
    creneauRepository.save(creneau);

    // Utiliser le mapper pour associer les collaborateurs au créneau
    List<CreneauCollaborateur> creneauCollaborateurs = creneauCreationMapper.toCreneauCollaborateurs(creneau, creneauDTO.getCollaborateursIds());
    creneauCollaborateurRepository.saveAll(creneauCollaborateurs);

    // Créer et enregistrer une notification pour chaque collaborateur
    for (CreneauCollaborateur creneauCollaborateur : creneauCollaborateurs) {
        User collaborateur = creneauCollaborateur.getCollaborateur();

        // Créer une notification
        Notification notification = new Notification();
        notification.setDestinataire(collaborateur);
        notification.setDateEnvoi(LocalDateTime.now());
        notification.setMessage("Les créneaux sont créés, veuillez choisir un créneau avant le " +
                LocalDate.now().plusDays(2) + " à minuit.");
        notificationRepository.save(notification);
    }
}


    public Creneau saveCreneau(Creneau creneau) {
        return creneauRepository.save(creneau);
    }

    public void notifyCollaborateurs(List<Long> collaborateursIds, Creneau creneau) {
        List<User> collaborateurs = userRepository.findAllById(collaborateursIds);

        String message = String.format(
                "Le Chargé RH a créé un créneau pour le %s à partir de %s jusqu'à %s. Veuillez choisir le créneau qui vous convient avant %s.",
                creneau.getDate(),
                creneau.getHeureDebutVisite(),
                creneau.getHeureFinVisite(),
                LocalDateTime.now().plusDays(2).toLocalDate() // Date limite pour choisir
        );

        for (User collaborateur : collaborateurs) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setLu(false);
            notification.setDateEnvoi(LocalDateTime.now());
            notification.setDestinataire(collaborateur);

            notificationService.saveNotification(notification);
        }
    }

    public Optional<Creneau> getCreneauById(Long id) {
        return creneauRepository.findById(id);
    }

    public List<Creneau> getAllCreneaux() {
        return creneauRepository.findAll();
    }

    public String supprimerCreneau(Long id) {
        try {
            Optional<Creneau> creneau = creneauRepository.findById(id);
            if (creneau.isPresent()) {
                creneauRepository.deleteById(id);
                return "Créneau supprimé avec succès.";
            } else {
                return "Créneau non trouvé.";
            }
        } catch (Exception e) {
            return "Erreur lors de la suppression du créneau : " + e.getMessage();
        }
    }

    public List<Creneau> getAllCreneauxCollab() {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null && currentUser.getRole() == Role.COLLABORATEUR) {
            return creneauRepository.findByCollaborateur_idUser(currentUser.getIdUser());
        }
        return List.of();
    }

}
