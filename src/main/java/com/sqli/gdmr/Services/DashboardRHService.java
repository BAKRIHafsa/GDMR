package com.sqli.gdmr.Services;

import com.sqli.gdmr.DTOs.DashboardRHDTO;
import com.sqli.gdmr.Enums.StatusVisite;
import com.sqli.gdmr.Mappers.DashboardRHMapper;
import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;




@Service
public class DashboardRHService {
    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private UserService userService;


    public List<DashboardRHDTO>  getAllVisitsDetailsCollab() {
        // List of statuses to include
        List<StatusVisite> statusesToInclude = Arrays.asList(
                StatusVisite.PLANIFIE,
                StatusVisite.EN_COURS,
                StatusVisite.TERMINE,
                StatusVisite.ANNULE,
                StatusVisite.NON_VALIDE,
                StatusVisite.CONFIRME_COLLAB,
                StatusVisite.CONFIRME_MED
        );

        // Retrieve all visits
        List<Creneau> allVisits = dashboardRepository.findAll();

        // Filter by status
        List<Creneau> filteredVisits = allVisits.stream()
                .filter(creneau -> statusesToInclude.contains(creneau.getStatusVisite()))
                .collect(Collectors.toList());

        // Map to DTO
        return filteredVisits.stream()
                .map(DashboardRHMapper::toDashboardRHDTO)
                .collect(Collectors.toList());
    }



    public List<DashboardRHDTO> getAllVisitsDetails() {
        // List of statuses to include
        List<StatusVisite> statusesToInclude = Arrays.asList(
                StatusVisite.PLANIFIE,
                StatusVisite.EN_COURS,
                StatusVisite.TERMINE,
                StatusVisite.ANNULE,
                StatusVisite.NON_VALIDE,
                StatusVisite.CONFIRME_COLLAB,
                StatusVisite.CONFIRME_MED
        );

        // Retrieve all visits
        List<Creneau> allVisits = dashboardRepository.findAll();

        // Filter by status
        List<Creneau> filteredVisits = allVisits.stream()
                .filter(creneau -> statusesToInclude.contains(creneau.getStatusVisite()))
                .collect(Collectors.toList());

        // Map to DTO
        return filteredVisits.stream()
                .map(DashboardRHMapper::toDashboardRHDTO)
                .collect(Collectors.toList());
    }


    public long countPlannedVisitsForToday() {
        LocalDate today = LocalDate.now();
        List<Creneau> allVisits = dashboardRepository.findByDate(today);

        return allVisits.stream()
                .filter(creneau -> creneau.getStatusVisite() != null && creneau.getStatusVisite().equals(StatusVisite.PLANIFIE))
                .count();
    }
    public long countPlannedVisitsForTodayCollab() {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("L'utilisateur connecté est introuvable.");
        }

        LocalDate today = LocalDate.now();

        List<Creneau> allVisits = dashboardRepository.findByDate(today);

        return allVisits.stream()
                .filter(creneau -> creneau.getStatusVisite() != null && creneau.getStatusVisite().equals(StatusVisite.PLANIFIE))
                .filter(creneau -> creneau.getCollaborateur() != null && creneau.getCollaborateur().getIdUser().equals(currentUser.getIdUser()))
                .count();
    }

    public long countCompletedVisitsForCurrentWeek() {
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Creneau> allVisits = dashboardRepository.findByDateBetween(startOfWeek, endOfWeek);

        return allVisits.stream()
                .filter(creneau -> creneau.getStatusVisite() != null && creneau.getStatusVisite().equals(StatusVisite.TERMINE))
                .count();
    }

    public long countCompletedVisitsForCurrentWeekCollab() {

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("L'utilisateur connecté est introuvable.");
        }
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Creneau> allVisits = dashboardRepository.findByDateBetween(startOfWeek, endOfWeek);

        return allVisits.stream()
                .filter(creneau -> creneau.getStatusVisite() != null && creneau.getStatusVisite().equals(StatusVisite.TERMINE))
                .filter(creneau -> creneau.getCollaborateur() != null && creneau.getCollaborateur().getIdUser().equals(currentUser.getIdUser()))
                .count();
    }

    public long countPlannedVisitsForTodayMed() {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("L'utilisateur connecté est introuvable.");
        }

        LocalDate today = LocalDate.now();

        List<Creneau> allVisits = dashboardRepository.findByDate(today);

        return allVisits.stream()
                .filter(creneau -> creneau.getStatusVisite() != null && creneau.getStatusVisite().equals(StatusVisite.PLANIFIE))
                .filter(creneau -> creneau.getMedecin() != null && creneau.getMedecin().getIdUser().equals(currentUser.getIdUser()))
                .count();
    }

    public long countCompletedVisitsForCurrentWeekMed() {

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("L'utilisateur connecté est introuvable.");
        }
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Creneau> allVisits = dashboardRepository.findByDateBetween(startOfWeek, endOfWeek);

        return allVisits.stream()
                .filter(creneau -> creneau.getStatusVisite() != null && creneau.getStatusVisite().equals(StatusVisite.TERMINE))
                .filter(creneau -> creneau.getMedecin() != null && creneau.getMedecin().getIdUser().equals(currentUser.getIdUser()))
                .count();
    }

    public List<DashboardRHDTO> getAllVisitsDetailsMed() {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("L'utilisateur connecté est introuvable.");
        }
        List<Creneau> allVisits = dashboardRepository.findAll();

        List<Creneau> userVisits = allVisits.stream()
                .filter(creneau -> creneau.getMedecin() != null && creneau.getMedecin().getIdUser().equals(currentUser.getIdUser()))
                .collect(Collectors.toList());

        return userVisits.stream()
                .map(DashboardRHMapper::toDashboardRHDTO)
                .collect(Collectors.toList());
    }


}
