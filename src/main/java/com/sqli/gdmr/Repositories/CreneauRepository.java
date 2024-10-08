package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CreneauRepository extends JpaRepository<Creneau, Long> {
    List<Creneau> findByCollaborateur_idUser(Long idUser);
    boolean existsByCollaborateurAndDateAndHeureDebutVisiteLessThanEqualAndHeureFinVisiteGreaterThanEqual(
            User collaborateur, LocalDate date, LocalTime heureFin, LocalTime heureDebut);

}

