package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
