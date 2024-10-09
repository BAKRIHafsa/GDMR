package com.sqli.gdmr.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String dossierUpload = "uploads/";

    public String sauvegarderFichier(MultipartFile fichier) throws IOException {
        // Créer le dossier d'uploads s'il n'existe pas
        File uploadDir = new File(dossierUpload);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Générer un nom unique pour éviter les conflits
        String nomFichierUnique = UUID.randomUUID().toString() + "_" + fichier.getOriginalFilename();

        // Construire le chemin du fichier
        Path cheminFichier = Paths.get(dossierUpload, nomFichierUnique);

        // Sauvegarder le fichier sur le système de fichiers
        Files.write(cheminFichier, fichier.getBytes());

        // Retourner le chemin relatif du fichier sauvegardé
        return cheminFichier.toString();
    }
}
