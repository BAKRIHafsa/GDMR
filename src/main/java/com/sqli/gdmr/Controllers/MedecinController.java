package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.DTOs.DashboardRHDTO;
import com.sqli.gdmr.Services.DashboardRHService;
import com.sqli.gdmr.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/med")
public class MedecinController {
    @Autowired
    private UserService userService;
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

}
