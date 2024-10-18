package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Enums.StatusVisite;
import com.sqli.gdmr.Enums.TypesVisite;
import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreneauRepository extends JpaRepository<Creneau, Long> {
    List<Creneau> findByCollaborateur_idUser(Long idUser);

    List<Creneau> findByStatusVisite(StatusVisite statusVisite);

    List<Creneau> findAll();

    @Query("SELECT c FROM Creneau c WHERE c.collaborateur.id = :collaborateurId " +
            "AND c.typeVisite = :typeVisite AND c.statusVisite = :statusVisite")
    Optional<Creneau> findByCollaborateurAndTypes(
            @Param("collaborateurId") Long collaborateurId,
            @Param("typeVisite") TypesVisite typeVisite,
            @Param("statusVisite") StatusVisite statusVisite
    );
    boolean existsByCollaborateurAndDateAndHeureDebutVisiteLessThanEqualAndHeureFinVisiteGreaterThanEqual(
            User collaborateur, LocalDate date, LocalTime heureFin, LocalTime heureDebut);

    List<Creneau> findByMedecinAndStatusVisiteIn(User medecin, List<StatusVisite> statuses);

    List<Creneau> findAllByStatusVisite(StatusVisite statusVisite);



}

