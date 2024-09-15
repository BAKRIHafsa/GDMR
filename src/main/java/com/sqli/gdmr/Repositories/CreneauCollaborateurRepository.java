package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Models.CreneauCollaborateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreneauCollaborateurRepository extends JpaRepository<CreneauCollaborateur, Long> {
}
