package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Models.Disponibilité;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisponibilitéRepository extends JpaRepository<Disponibilité, Long> {
    List<Disponibilité> findByMedecin_idUser(Long medecinId);
    List<Disponibilité> findByDate(LocalDate date);

}
