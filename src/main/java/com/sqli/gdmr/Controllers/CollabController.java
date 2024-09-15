package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.DTOs.DashboardRHDTO;
import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Services.CreneauService;
import com.sqli.gdmr.Services.DashboardRHService;
import com.sqli.gdmr.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
