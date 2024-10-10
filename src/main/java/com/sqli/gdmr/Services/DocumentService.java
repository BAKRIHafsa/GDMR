package com.sqli.gdmr.Services;

import com.sqli.gdmr.Models.Document;
import com.sqli.gdmr.Repositories.DocumentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    // Méthode pour récupérer les documents d'un créneau spécifique
//    public List<Document> getDocumentsByCreneauId(Long creneauId) {
//        return documentRepository.findByCreneauId(creneauId);
//    }
}
