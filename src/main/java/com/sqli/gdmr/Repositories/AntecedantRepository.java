package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Models.Antecedant;
import com.sqli.gdmr.Models.Collaborateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AntecedantRepository extends JpaRepository<Antecedant, Long> {
    Optional<Antecedant> findByCollaborateur(Collaborateur collaborateur);

}
