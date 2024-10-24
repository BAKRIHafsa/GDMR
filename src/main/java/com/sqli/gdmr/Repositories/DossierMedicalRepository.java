package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Models.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {
    // Méthode pour trouver un dossier médical par id du créneau
    DossierMedical findByCreneau_IdCréneau(Long id);
    List<DossierMedical> findByCollaborateur_idUser(Long id); // Utiliser l'ID hérité de User

}
