package com.sqli.gdmr.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sqli.gdmr.Models.Medecin;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {

    @Query("SELECT m FROM Medecin m " +
            "JOIN Disponibilité d ON m.id = d.medecin.id " +
            "WHERE d.date = :date AND " +
            "d.heuredebut <= :heuredebut AND d.heurefin >= :heurefin AND " +
            "m.id NOT IN (SELECT c.medecin.id FROM Creneau c " +
            "WHERE c.date = :date AND " +
            "((c.heureDebutVisite <= :heurefin AND c.heureFinVisite >= :heuredebut)))")
    List<Medecin> findAvailableMedecins(@Param("date") LocalDate date,
                                        @Param("heuredebut") LocalTime heuredebut,
                                        @Param("heurefin") LocalTime heurefin);


}

