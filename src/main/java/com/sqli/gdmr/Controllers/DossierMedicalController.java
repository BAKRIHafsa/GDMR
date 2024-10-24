package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.Models.DossierMedical;
import com.sqli.gdmr.Services.DossierMedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dossiers-medicaux")
public class DossierMedicalController {

    @Autowired
    private DossierMedicalService dossierMedicalService;
    @PostMapping("/ajouter")
    public ResponseEntity<?> ajouterDossierMedical(@RequestBody DossierMedical dossierMedical,@RequestParam Long idCollaborateur) {
        try {
            DossierMedical newDossier = dossierMedicalService.ajouterDossierMedical(dossierMedical,idCollaborateur);
            return ResponseEntity.ok(newDossier);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Creneau not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // Endpoint pour récupérer un dossier médical par créneau
    @GetMapping("/creneau/{idCreneau}")
    public DossierMedical obtenirDossierParCreneau(@PathVariable Long idCreneau) {
        return dossierMedicalService.obtenirDossierParCreneau(idCreneau);
    }
    @GetMapping("/collaborateur/{idCollaborateur}")
    public ResponseEntity<List<DossierMedical>> obtenirDossiersParCollaborateur(@PathVariable Long idCollaborateur) {
        List<DossierMedical> dossiers = dossierMedicalService.obtenirDossiersParCollaborateur(idCollaborateur);
        return ResponseEntity.ok(dossiers);
    }

    @PutMapping("/modifier/{id}")
    public DossierMedical modifierDossierMedical(@PathVariable Long id, @RequestBody DossierMedical dossierMedicalDetails) {
        return dossierMedicalService.modifierDossierMedical(id, dossierMedicalDetails);
    }
}
