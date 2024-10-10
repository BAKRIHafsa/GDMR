package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.DTOs.CreneauCreationDTO;
import com.sqli.gdmr.Enums.StatusVisite;
import com.sqli.gdmr.Mappers.CreneauCreationMapper;
import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Services.CreneauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/creneaux")
public class CreneauController {

    @Autowired
    private CreneauService creneauService;

//    @PostMapping("/creer")
//    public ResponseEntity<Map<String, String>> creerCreneau(@RequestBody Creneau creneau) {
//        String message = creneauService.creerCreneauParChargeRh(creneau);
//        Map<String, String> response = new HashMap<>();
//        response.put("message", message);
//        if (message.contains("succès")) {
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//    }
//@PostMapping("/creer")
//public ResponseEntity<String> createCreneau(@RequestBody CreneauCreationDTO creneauCreationDTO) {
//    Creneau creneau = CreneauCreationMapper.fromDTO(creneauCreationDTO);
//
//    Creneau createdCreneau = creneauService.saveCreneau(creneau);
//
//    creneauService.notifyCollaborateurs(creneauCreationDTO.getCollaborateursIds(), createdCreneau);
//
//    return ResponseEntity.ok("Créneau créé et notifications envoyées avec succès.");
//}
//@PostMapping("/create")
//public ResponseEntity<String> createCreneau(@RequestBody CreneauCreationDTO creneauCreationDTO) {
//    try {
//        creneauService.createCreneauAndNotify(creneauCreationDTO);
//        return ResponseEntity.ok("Créneau créé avec succès et notifications envoyées.");
//    } catch (Exception e) {
//        return ResponseEntity.status(500).body("Erreur lors de la création du créneau: " + e.getMessage());
//    }
//}
@PostMapping("/creer")
public ResponseEntity<String> createCreneau(@RequestBody CreneauCreationDTO creneauDTO) {
    creneauService.creerCreneauEtEnvoyerNotifications(creneauDTO);
    return ResponseEntity.ok("Créneau créé avec succès et notifications envoyées."); // Return plain text
}

    @PutMapping("/visites-spontanees/{collaborateurId}")
    public ResponseEntity<Void> mettreAJourCreneauVisiteSpontanee(
            @PathVariable Long collaborateurId,
            @Valid @RequestBody CreneauCreationDTO creneauCreationDTO) {
        creneauService.mettreAJourCreneauVisiteSpontanee(collaborateurId, creneauCreationDTO);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/planifier-visite-spontanee/{id}")
    public ResponseEntity<Creneau> planifierVisiteSpontanee(
            @PathVariable Long id,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("heureDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDebut,
            @RequestParam("heureFin") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureFin) {

        Creneau creneau = creneauService.planifierVisiteSpontanee(id, date, heureDebut, heureFin);
        return new ResponseEntity<>(creneau, HttpStatus.OK);
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Creneau> modifierStatutCreneau(@PathVariable Long id, @RequestBody Map<String, String> statutMap) {
        String nouveauStatut = statutMap.get("statut");
        Creneau creneau = creneauService.modifierStatutCreneau(id, StatusVisite.valueOf(nouveauStatut.toUpperCase()));
        return ResponseEntity.ok(creneau);
    }


    @GetMapping("/affiche")
    public ResponseEntity<List<Creneau>> getCreneaux() {
        List<Creneau> creneaux = creneauService.getAllCreneaux();
        return ResponseEntity.ok(creneaux);
    }


    @GetMapping("/all-visites-planifiées")
    public ResponseEntity<List<Creneau>> getCreneauxPlanifies() {
        List<Creneau> creneaux = creneauService.getAllCreneauxPlanifies();
        return ResponseEntity.ok(creneaux);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Creneau>> getCreneauById(@PathVariable Long id) {
        Optional<Creneau> creneau = creneauService.getCreneauById(id);
        if (creneau.isPresent()) {
            return ResponseEntity.ok(creneau);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.empty());
        }
    }


    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<String> supprimerCreneau(@PathVariable Long id) {
        String message = creneauService.supprimerCreneau(id);
        if (message.contains("succès")) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @PostMapping("/{id}/valider")
    public ResponseEntity<?> validerCreneau(@PathVariable Long id) {
        try {
            creneauService.validerCreneau(id);
            return ResponseEntity.ok().body("Le créneau a été validé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la validation du créneau.");
        }
    }

    @PostMapping("/{id}/non-valider")
    public ResponseEntity<?> nonValiderCreneau(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody
    ) {
        String justification = requestBody.get("justification");

        try {
            creneauService.nonValiderCreneau(id, justification);
            return ResponseEntity.ok().body("Le créneau a été non validé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la non-validation du créneau.");
        }
    }
}

