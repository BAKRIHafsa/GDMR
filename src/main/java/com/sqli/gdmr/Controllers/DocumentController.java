package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.Models.Document;
import com.sqli.gdmr.Services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    // Endpoint pour récupérer les documents d'un créneau spécifique
//    @GetMapping("/creneau/{creneauId}")
//    public ResponseEntity<List<Document>> getDocumentsByCreneauId(@PathVariable Long creneauId) {
//        List<Document> documents = documentService.getDocumentsByCreneauId(creneauId);
//        if (documents.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(documents);
//        }
//    }
}
