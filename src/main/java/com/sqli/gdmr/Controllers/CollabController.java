package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.DTOs.CreneauRequestDTO;
import com.sqli.gdmr.DTOs.DashboardRHDTO;
import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Services.CreneauService;
import com.sqli.gdmr.Services.DashboardRHService;
import com.sqli.gdmr.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/collab")
public class CollabController {
    @Autowired
    private UserService userService;
    @Autowired
    private DashboardRHService dashboardRHService;

    @Autowired
    private CreneauService creneauService;

    @GetMapping("/planned-today-count")
    public ResponseEntity<Long> getPlannedVisitsCountForTodayCollab() {
        long count = dashboardRHService.countPlannedVisitsForTodayCollab();
        return ResponseEntity.ok(count);
    }

    @PutMapping("/annuler/{id}")
    public ResponseEntity<?> annulerCreneau(@PathVariable Long id, @RequestBody String motifAnnulation) {
        try {
            creneauService.annulerCreneauEtEnvoyerNotification(id, motifAnnulation);
            return ResponseEntity.ok().body("Visite annulée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @GetMapping("/completed-week-count")
    public ResponseEntity<Long> getCompletedVisitsCountForCurrentWeekCollab() {
        long count = dashboardRHService.countCompletedVisitsForCurrentWeekCollab();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DashboardRHDTO>> getAllVisitsDetailsCollab() {
        List<DashboardRHDTO> visitsDetails = dashboardRHService.getAllVisitsDetailsCollab();
        return ResponseEntity.ok(visitsDetails);
    }

    @GetMapping("/affiche")
    public ResponseEntity<List<Creneau>> getCreneauxCollab() {
        List<Creneau> creneaux = creneauService.getAllCreneauxCollab();
        return ResponseEntity.ok(creneaux);
    }

    @PostMapping("/{id}/valider")
    public ResponseEntity<Map<String, String>> validerCreneau(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        try {
            boolean isValide = creneauService.validerCreneau(id);
            System.out.println("Validation du créneau: " + isValide); // Log pour voir le résultat de la validation

            if (isValide) {
                response.put("message", "Le créneau a été validé avec succès.");
                return ResponseEntity.ok(response);  // Renvoie une réponse JSON
            } else {
                response.put("message", "Le créneau n'a pas pu être validé.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Renvoie une réponse JSON
            }
        } catch (NoSuchElementException e) {
            response.put("message", "Creneau non trouvé.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log de l'exception pour débogage
            response.put("message", "Erreur lors de la validation du créneau.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    // Non-valider le créneau avec justification
    @PostMapping("/{id}/non-valider")
    public ResponseEntity<?> nonValiderCreneau(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody
    ) {
        String justification = requestBody.get("justification");

        try {
            creneauService.nonValiderCreneau(id, justification);
            System.out.println("Creneau non validé avec succès");
            // Retournez un JSON explicite plutôt qu'une simple chaîne
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Le créneau a été non validé avec succès."));
        } catch (Exception e) {
            e.printStackTrace();  // Ajoutez ce log pour voir le message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Erreur lors de la non-validation du créneau."));
        }
    }

    @PostMapping("/creer-visite-spontanee")
    public ResponseEntity<Creneau> creerVisiteSpontanee(
            @RequestParam("motif") String motif,
            @RequestParam(value = "fichiers", required = false) List<MultipartFile> fichiers // Rendre ce paramètre optionnel
    ) {
        CreneauRequestDTO request = new CreneauRequestDTO();
        request.setMotif(motif);
        try {
            Creneau creneau = creneauService.creerVisiteSpontanee(request, fichiers);
            return new ResponseEntity<>(creneau, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}


