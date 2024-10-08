package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.Models.Antecedant;
import com.sqli.gdmr.Services.AntecedantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/antecedants")
public class AntecedantController {
    @Autowired
    private AntecedantService antecedantService;

    @GetMapping("/obtenir")
    public ResponseEntity<Antecedant> getAntecedantForCurrentUser() {
        Antecedant antecedant = antecedantService.getAntecedantForCurrentUser();
        if (antecedant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(antecedant, HttpStatus.OK);
    }


    @PostMapping("/ajouter")
    public ResponseEntity<Antecedant> createAntecedant(@RequestBody Antecedant antecedant) {
        Antecedant savedAntecedant = antecedantService.saveAntecedant(antecedant);
        return ResponseEntity.ok(savedAntecedant);
    }

    @PutMapping("/modifier/{antecedantId}")
    public ResponseEntity<Antecedant> updateAntecedant(@PathVariable Long antecedantId, @RequestBody Antecedant antecedant) {
        Antecedant updatedAntecedant = antecedantService.updateAntecedant(antecedantId, antecedant);
        return new ResponseEntity<>(updatedAntecedant, HttpStatus.OK);
    }
}
