package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.DTOs.DashboardRHDTO;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Services.DashboardRHService;
import com.sqli.gdmr.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chargéRH")
public class ChargéRHController {
    @Autowired
    private UserService userService;
    @Autowired
    private DashboardRHService dashboardRHService;

    @GetMapping("/ACollaborateurs")
    public ResponseEntity<Map<String, List<User>>> getActiveCollaborateurs() {
        try {
            Map<String, List<User>> users = userService.getActiveCollaborateurs();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            // Log the exception and return a meaningful error response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/AMedecins")
    //@PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<Map<String, List<User>>> getActiveMedecins() {
        Map<String, List<User>> users = userService.getActiveMedecins();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/planned-today-count")
    public ResponseEntity<Long> getPlannedVisitsCountForToday() {
        long count = dashboardRHService.countPlannedVisitsForToday();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/completed-week-count")
    public ResponseEntity<Long> getCompletedVisitsCountForCurrentWeek() {
        long count = dashboardRHService.countCompletedVisitsForCurrentWeek();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DashboardRHDTO>> getAllVisitsDetails() {
        List<DashboardRHDTO> visitsDetails = dashboardRHService.getAllVisitsDetails();
        return ResponseEntity.ok(visitsDetails);
    }
    @GetMapping("/users/{id}")
    //@PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

//    @GetMapping("/planned-today-count-RH")
//    public ResponseEntity<Long> getPlannedVisitsCountForTodayRH() {
//        long count = dashboardRHService.countPlannedVisitsForTodayCollab();
//        return ResponseEntity.ok(count);
//    }
//
//    @GetMapping("/completed-week-count-RH")
//    public ResponseEntity<Long> getCompletedVisitsCountForCurrentWeekRH() {
//        long count = dashboardRHService.countCompletedVisitsForCurrentWeekCollab();
//        return ResponseEntity.ok(count);
//    }
//
//    @GetMapping("/all-RH")
//    public ResponseEntity<List<DashboardRHDTO>> getAllVisitsDetailsRH() {
//        List<DashboardRHDTO> visitsDetails = dashboardRHService.getAllVisitsDetailsCollab();
//        return ResponseEntity.ok(visitsDetails);
//    }
}
