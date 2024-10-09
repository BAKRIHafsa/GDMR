package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.DTOs.DashboardRHDTO;
import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Services.CreneauService;
import com.sqli.gdmr.Services.DashboardRHService;
import com.sqli.gdmr.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
