package com.sqli.gdmr.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sqli.gdmr.Models.Medecin;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {

    @Query("SELECT m FROM Medecin m WHERE m.id NOT IN " + "(SELECT d.medecin.id FROM Disponibilité d " + "WHERE d.date = :date AND " + "((d.heuredebut <= :heurefin AND d.heurefin >= :heuredebut)))")
    List<Medecin> findAvailableMedecins(@Param("date") LocalDate date,
                                        @Param("heuredebut") LocalTime heuredebut,
                                        @Param("heurefin") LocalTime heurefin);
}

