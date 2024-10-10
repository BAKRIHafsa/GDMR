package com.sqli.gdmr.Controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileDownloadController {

    @Value("${file.upload-dir}")
    private String dossierUpload;

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            // Construire le chemin complet du fichier en utilisant le dossier configuré
            Path cheminFichier = Paths.get(dossierUpload).resolve(filename).normalize();
            Resource resource = new UrlResource(cheminFichier.toUri());

            // Vérifier si le fichier existe et est lisible
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Retourner le fichier avec les en-têtes HTTP appropriés
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace(); // Pour déboguer
            return ResponseEntity.internalServerError().build();
        }
    }


}
