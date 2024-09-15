package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Models.Creneau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Creneau, Long>{
    List<Creneau> findByDate(LocalDate date);

    List<Creneau> findByDateBetween(LocalDate startDate, LocalDate endDate);


}
