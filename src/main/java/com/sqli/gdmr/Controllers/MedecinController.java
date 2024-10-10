package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.DTOs.DashboardRHDTO;
import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Models.Medecin;
import com.sqli.gdmr.Services.CreneauService;
import com.sqli.gdmr.Services.DashboardRHService;
import com.sqli.gdmr.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/med")
public class MedecinController {
    @Autowired
    private UserService userService;
    private static final Logger log = LoggerFactory.getLogger(MedecinController.class);

    @Autowired
    private CreneauService creneauService;

    @Autowired
    private DashboardRHService dashboardRHService;
    @GetMapping("/planned-today-count")
    public ResponseEntity<Long> getPlannedVisitsCountForTodayMed() {
        long count = dashboardRHService.countPlannedVisitsForTodayMed();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/completed-week-count")
    public ResponseEntity<Long> getCompletedVisitsCountForCurrentWeekMed() {
        long count = dashboardRHService.countCompletedVisitsForCurrentWeekMed();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DashboardRHDTO>> getAllVisitsDetailsMed() {
        List<DashboardRHDTO> visitsDetails = dashboardRHService.getAllVisitsDetailsMed();
        return ResponseEntity.ok(visitsDetails);
    }

    @GetMapping("/disponibles")
    public List<Medecin> getMedecinsDisponibles(@RequestParam LocalDate date, @RequestParam LocalTime heureDebut, @RequestParam LocalTime heureFin) {
        return userService.findMedecinsDisponibles(date, heureDebut, heureFin);
    }

    @GetMapping("/creneaux")
    public ResponseEntity<List<Creneau>> getCreneauxForCurrentMedecin() {
        try {
            List<Creneau> creneaux = creneauService.getCreneauxForCurrentMedecin();
            return ResponseEntity.ok(creneaux);
        } catch (RuntimeException e) {
            log.error("Error fetching creneaux", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
