package com.sqli.gdmr.Services;
import java.io.IOException;
import java.util.List;

import com.sqli.gdmr.Repositories.*;
import org.springframework.web.multipart.MultipartFile;


import com.sqli.gdmr.DTOs.CreneauCreationDTO;
import com.sqli.gdmr.DTOs.CreneauRequestDTO;
import com.sqli.gdmr.Enums.Role;
import com.sqli.gdmr.Enums.StatusVisite;
import com.sqli.gdmr.Enums.TypesVisite;
import com.sqli.gdmr.Mappers.CreneauCreationMapper;
import com.sqli.gdmr.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CreneauService {

    @Autowired
    private CreneauRepository creneauRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DisponibilitéService disponibilitéService;

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

    List<Medecin> medecinsDisponibles = medecinRepository.findAvailableMedecins(
            creneau.getDate(),
            creneau.getHeureDebutVisite(),
            creneau.getHeureFinVisite()
    );

    if (medecinsDisponibles.isEmpty()) {
        // Gérer le cas où aucun médecin n'est disponible
        throw new IllegalArgumentException("Le créneau sélectionné n'est plus disponible chez aucun médecin.");
    }

    Medecin medecinSelectionne = medecinsDisponibles.get(0); // Vous pouvez affiner la logique si nécessaire
    creneau.setMedecin(medecinSelectionne);

    // Vérifier si le créneau est disponible pour au moins un médecin
    boolean isAvailable = disponibilitéService.isCreneauAvailable(
            creneau.getDate(),
            creneau.getHeureDebutVisite(),
            creneau.getHeureFinVisite()
    );

    if (!isAvailable) {
        // Gérer le cas où aucun médecin n'est disponible
        throw new IllegalArgumentException("Le créneau sélectionné n'est plus disponible chez aucun médecin.");
    }

    // Associer le collaborateur au créneau
    User userCollaborateur = userService.findById(creneauDTO.getCollaborateurId());
    if (userCollaborateur == null) {
        throw new IllegalArgumentException("Collaborateur non trouvé");
    }


    if (!(userCollaborateur instanceof Collaborateur)) {
        throw new IllegalArgumentException("L'utilisateur spécifié n'est pas un collaborateur.");
    }
    Collaborateur collaborateur = (Collaborateur) userCollaborateur;
    creneau.setCollaborateur(collaborateur);

    // Associer le chargé RH au créneau
    User currentUser = userService.getCurrentUser(); // Use a method to get the current authenticated user
    if (currentUser == null || currentUser.getRole() != Role.CHARGE_RH) {
        throw new IllegalArgumentException("L'utilisateur actuellement authentifié n'est pas un chargé RH.");
    }

    creneau.setChargeRh(currentUser);

    // Sauvegarder le créneau
    creneauRepository.save(creneau);

    // Créer et enregistrer une notification pour le collaborateur
    Notification notification = new Notification();
    notification.setDestinataire(collaborateur);
    notification.setDateEnvoi(LocalDateTime.now());
    notification.setMessage("Un créneau a été créé par " + currentUser.getNom() +
            ". Veuillez consulter le calendrier pour confirmer votre visite avant le " +
            LocalDate.now().plusDays(2) + " à minuit.");
    notificationRepository.save(notification);
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

    public List<Creneau> getAllCreneauxPlanifies() {
        return creneauRepository.findByStatusVisite(StatusVisite.PLANIFIE);
    }

    public boolean validerCreneau(Long id) throws Exception {
        Creneau creneau = creneauRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Créneau introuvable"));

        // Vérifiez l'état du créneau
        if (!creneau.getStatusVisite().equals(StatusVisite.EN_ATTENTE_VALIDATION)) {
            throw new Exception("Le créneau n'est pas en attente de validation.");
        }

        // Modifier l'état du créneau
        creneau.setStatusVisite(StatusVisite.VALIDE);
        creneauRepository.save(creneau);

        // Récupérer le Chargé RH à partir du créneau
        User chargeRH = creneau.getChargeRh();

        if (chargeRH != null) {
            // Envoyer une notification au Chargé RH
            notificationService.sendNotification(chargeRH, "Le créneau " + creneau.getIdCréneau() + " a été validé.");
        } else {
            throw new Exception("Aucun Chargé RH assigné à ce créneau.");
        }

        return true;
    }

    public boolean nonValiderCreneau(Long id, String justification) throws Exception {
        Creneau creneau = creneauRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Créneau introuvable"));

        // Vérifiez l'état du créneau
        if (!creneau.getStatusVisite().equals(StatusVisite.EN_ATTENTE_VALIDATION)) {
            throw new Exception("Le créneau n'est pas en attente de validation.");
        }

        // Modifier l'état du créneau et ajouter la justification
        creneau.setStatusVisite(StatusVisite.NON_VALIDE);
        creneau.setJustifNonValide(justification);
        creneauRepository.save(creneau);

        // Récupérer le Chargé RH à partir du créneau
        User chargeRH = creneau.getChargeRh();

        if (chargeRH != null) {
            // Envoyer une notification au Chargé RH
            notificationService.sendNotification(chargeRH, "Le créneau " + creneau.getIdCréneau() + " n'a pas été validé. Justification : " + justification);
        } else {
            throw new Exception("Aucun Chargé RH assigné à ce créneau.");
        }

        return true;
    }

//    public Creneau creerVisiteSpontanee(CreneauRequestDTO request) {
//        // Récupérer l'utilisateur authentifié (collaborateur)
//        User currentUser = userService.getCurrentUser();
//
//        // Création d'un nouvel objet Creneau
//        Creneau creneau = new Creneau();
//
//        // Configuration des propriétés du Creneau
//        creneau.setDate(LocalDate.now()); // Date de la visite
//        creneau.setMotif(request.getMotif()); // Motif de la visite
//        creneau.setTypeVisite(TypesVisite.VISITE_SPONTANEE); // Type de visite : "visite spontanée"
//        creneau.setStatusVisite(StatusVisite.EN_ATTENTE_CREATION_CRENEAU); // Statut de la visite
//
//        // Lier le collaborateur actuel au creneau
//        Collaborateur collaborateur = new Collaborateur();
//        collaborateur.setIdUser(currentUser.getIdUser());
//        creneau.setCollaborateur(collaborateur);
//
//        // Enregistrement du creneau dans la base de données
//        Creneau savedCreneau = creneauRepository.save(creneau);
//
//        // Notifier tous les chargés RH
//        notifyChargeRH(currentUser);
//
//        return savedCreneau;
//    }
public Creneau creerVisiteSpontanee(CreneauRequestDTO request, List<MultipartFile> fichiers) throws IOException {
    User currentUser = userService.getCurrentUser();
    Creneau creneau = new Creneau();
    creneau.setMotif(request.getMotif());
    creneau.setTypeVisite(TypesVisite.VISITE_SPONTANEE);
    creneau.setStatusVisite(StatusVisite.EN_ATTENTE_CREATION_CRENEAU);

    Collaborateur collaborateur = new Collaborateur();
    collaborateur.setIdUser(currentUser.getIdUser());
    creneau.setCollaborateur(collaborateur);

    Creneau savedCreneau = creneauRepository.save(creneau);

    if (fichiers != null && !fichiers.isEmpty()) {
        for (MultipartFile fichier : fichiers) {
            if (fichier != null && !fichier.isEmpty()) { // Vérifiez également si le fichier n'est pas vide
                String cheminFichier = fileStorageService.sauvegarderFichier(fichier);

                Document document = new Document();
                document.setNomFichier(fichier.getOriginalFilename());
                document.setTypeFichier(fichier.getContentType());
                document.setTailleFichier(fichier.getSize());
                document.setCheminFichier(cheminFichier);
                document.setCreneau(savedCreneau);
                documentRepository.save(document);
            }
        }
    }

    return savedCreneau;
}


    public void notifyChargeRH(User currentUser) {
        // Récupérer tous les utilisateurs avec le rôle de "Chargé RH"
        List<User> chargeRHList = userRepository.findByRole(Role.CHARGE_RH);

        // Créer une notification pour chaque chargé RH
        String message = "Le collaborateur " + currentUser.getNom() + " " + currentUser.getPrenom() +
                " a demandé une visite spontanée. Veuillez lui créer un créneau horaire.";

        for (User chargeRH : chargeRHList) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDateEnvoi(LocalDateTime.now());
            notification.setDestinataire(chargeRH); // Associer la notification à ce chargé RH

            // Enregistrer la notification
            notificationRepository.save(notification);
        }
    }
}
