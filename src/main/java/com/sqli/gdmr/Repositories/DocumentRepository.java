package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByCreneau_IdCréneau(Long idCréneau);

}
