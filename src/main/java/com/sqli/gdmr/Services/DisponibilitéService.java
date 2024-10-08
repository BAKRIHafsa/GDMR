package com.sqli.gdmr.Services;

import com.sqli.gdmr.Models.Disponibilité;
import com.sqli.gdmr.Models.Medecin;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.DisponibilitéRepository;
import com.sqli.gdmr.Repositories.MedecinRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class DisponibilitéService {

    @Autowired
    private DisponibilitéRepository disponibilitéRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MedecinRepository medecinRepository;


    //    public Disponibilité ajouterDisponibilite(Disponibilité disponibilite) {
//        User currentUser = userService.getCurrentUser();
//
//        if (currentUser instanceof Medecin) {
//            Medecin medecin = (Medecin) currentUser;
//            disponibilite.setMedecin(medecin);
//            return disponibilitéRepository.save(disponibilite);
//        } else {
//            throw new IllegalStateException("L'utilisateur connecté n'est pas un médecin.");
//        }
//    }
    public Disponibilité ajouterDisponibilite(Disponibilité disponibilite) {
        User currentUser = userService.getCurrentUser();

        log.info("Tentative d'ajout de disponibilité par l'utilisateur: {}", currentUser != null ? currentUser.getIdUser() : "null");

        if (currentUser == null) {
            log.error("Aucun utilisateur connecté");
            throw new IllegalStateException("Aucun utilisateur connecté.");
        }

        // Chercher explicitement le médecin correspondant à cet utilisateur
        Medecin medecin = medecinRepository.findById(currentUser.getIdUser())
                .orElseThrow(() -> {
                    log.error("Aucun médecin trouvé pour l'utilisateur {}", currentUser.getIdUser());
                    return new IllegalStateException("Aucun médecin trouvé pour cet utilisateur.");
                });

        disponibilite.setMedecin(medecin);

        log.info("Ajout de disponibilité pour le médecin: {}", medecin.getIdUser());
        return disponibilitéRepository.save(disponibilite);
    }

    public List<Disponibilité> getDisponibilitesByMedecin(Long medecinId) {
        return disponibilitéRepository.findByMedecin_idUser(medecinId);
    }

    public List<Disponibilité> getDisponibilitesByCurrentMedecin() {
        User currentUser = userService.getCurrentUser();
        if (currentUser instanceof Medecin) {
            Medecin medecin = (Medecin) currentUser;
            return disponibilitéRepository.findByMedecin_idUser(medecin.getIdUser());
        } else {
            throw new IllegalStateException("L'utilisateur connecté n'est pas un médecin.");
        }
    }

    public void supprimerDisponibilite(Long id) {
        disponibilitéRepository.deleteById(id);
    }

    public Disponibilité getDisponibiliteById(Long id) {
        return disponibilitéRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilité non trouvée avec l'ID: " + id));
    }

    public boolean isCreneauAvailable(LocalDate date, LocalTime heureDebutVisite, LocalTime heureFinVisite) {
        // Rechercher les disponibilités pour la date et vérifier si une disponibilité chevauche le créneau proposé
        List<Disponibilité> disponibilites = disponibilitéRepository.findByDate(date);

        for (Disponibilité disponibilite : disponibilites) {
            LocalTime debutDispo = disponibilite.getHeuredebut();
            LocalTime finDispo = disponibilite.getHeurefin();

            // Vérification des chevauchements
            if (!(heureFinVisite.isBefore(debutDispo) || heureDebutVisite.isAfter(finDispo))) {
                return true; // Il y a un médecin disponible pour ce créneau
            }
        }

        return false; // Aucun médecin disponible pour ce créneau
    }

    public List<Disponibilité> getAllDisponibilites() {
        return disponibilitéRepository.findAll();
    }

}
