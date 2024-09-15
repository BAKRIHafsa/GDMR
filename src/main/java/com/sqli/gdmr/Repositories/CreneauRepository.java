package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Models.Creneau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreneauRepository extends JpaRepository<Creneau, Long> {
    List<Creneau> findByCollaborateur_idUser(Long idUser);

}

