package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.Models.Disponibilité;
import com.sqli.gdmr.Services.DisponibilitéService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilites")
public class DisponibilitéController {

    @Autowired
    private DisponibilitéService disponibilitéService;

    @PostMapping("/ajouter")
    public ResponseEntity<Disponibilité> ajouterDisponibilite(@RequestBody Disponibilité disponibilite) {
        try {
            Disponibilité nouvelleDisponibilite = disponibilitéService.ajouterDisponibilite(disponibilite);
            return ResponseEntity.ok(nouvelleDisponibilite);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<Disponibilité>> getDisponibilitesByMedecin(@PathVariable Long medecinId) {
        return ResponseEntity.ok(disponibilitéService.getDisponibilitesByMedecin(medecinId));
    }

    @GetMapping("/current")
    public ResponseEntity<List<Disponibilité>> getDisponibilitesByCurrentMedecin() {
        try {
            List<Disponibilité> disponibilites = disponibilitéService.getDisponibilitesByCurrentMedecin();
            return ResponseEntity.ok(disponibilites);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<String> supprimerDisponibilite(@PathVariable Long id) {
        try {
            disponibilitéService.supprimerDisponibilite(id);
            return ResponseEntity.ok("Disponibilité supprimée avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : Disponibilité non trouvée.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disponibilité> getDisponibiliteById(@PathVariable Long id) {
        return ResponseEntity.ok(disponibilitéService.getDisponibiliteById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Disponibilité>> getAllDisponibilites() {
        List<Disponibilité> disponibilites = disponibilitéService.getAllDisponibilites();
        return ResponseEntity.ok(disponibilites);
    }
}
