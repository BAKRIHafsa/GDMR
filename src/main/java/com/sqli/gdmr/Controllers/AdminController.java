package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.DTOs.CreateMedecinRequestDTO;
import com.sqli.gdmr.DTOs.ModifieCollaborateur;
import com.sqli.gdmr.Models.Collaborateur;
import com.sqli.gdmr.Models.Medecin;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/ACCollaborateursAndChargeRH")
    //@PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<Map<String, List<User>>> getActiveAndCreatedCollaborateursAndChargeRH() {
        Map<String, List<User>> users = userService.getActiveAndCreatedCollaborateursAndChargeRH();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/ACMedecins")
    //@PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<Map<String, List<User>>> getActiveAndCreatedMedecins() {
        Map<String, List<User>> users = userService.getActiveAndCreatedMedecins();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/ACollaborateursAndChargeRH")
    //@PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<List<User>> getArchivedCollaborateursAndChargeRH() {
        List<User> users = userService.getArchivedCollaborateursAndChargeRH();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/AMedecins")
    //@PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<List<User>> getArchivedMedecins() {
        List<User> users = userService.getArchivedMedecins();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/users/{id}")
    //@PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
//@PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<Map<String, String>> creerMedecin(@RequestBody CreateMedecinRequestDTO request) {
        try {
            // Appel du service pour créer le médecin
            userService.createMedecinUser(request);

            // Créer une réponse JSON structurée
            Map<String, String> response = new HashMap<>();
            response.put("message", "Médecin créé avec succès");

            // Retourne un statut 200 avec le message
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Données invalides : " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Erreur serveur lors de la création du médecin"));
        }
    }
//    public ResponseEntity<String> createMedecin(@RequestBody Map<String, Object> requestBody) {
//        Long userId = ((Number) requestBody.get("idUser")).longValue();
//        String username = (String) requestBody.get("username"); // Ajout du nom d'utilisateur
//        String experience = (String) requestBody.get("experience");
//        String qualification = (String) requestBody.get("qualification");
//        String siteTravail = (String) requestBody.get("siteTravail");
//
//        try {
//            // Appel au service avec le nom d'utilisateur ajouté
//            String createdUser = userService.createMedecinUser(userId, username, experience, qualification, siteTravail);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }


    @PutMapping("/users/{id}")
    //@PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody ModifieCollaborateur user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/users/{id}/archive")
    //@PreAuthorize("hasAuthority('ADMINISTRATEUR')")
    public ResponseEntity<User> archiveUser(@PathVariable Long id) {
        User archivedUser = userService.archiveUser(id);
        return ResponseEntity.ok(archivedUser);
    }

//    @PostMapping("/create")
//    public User createUser(@RequestBody User userCreation) {
//        return userService.createUser(userCreation);
//    }

    @PostMapping("/create")
    public ResponseEntity<Collaborateur> createCollaborateur(@RequestBody Collaborateur collaborateur) {
        // Appeler le service pour créer le collaborateur
        Collaborateur createdCollaborateur = userService.createCollaborateur(collaborateur);

        // Retourner une réponse HTTP 201 avec le collaborateur créé
        return ResponseEntity.status(201).body(createdCollaborateur);
    }

    @PutMapping("/activer/{id}")
    public ResponseEntity<Map<String, String>> activerMedecin(@PathVariable Long id) {
        String result = userService.activerMedecin(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", result);

        if (result.equals("Le médecin a été activé avec succès.")) {
            return ResponseEntity.ok(response);
        } else if (result.equals("Médecin non trouvé.")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @PutMapping("/activerCollab/{id}")
    public ResponseEntity<Map<String, String>> activerUtilisateur(@PathVariable Long id) {
        String result = userService.activerCollaborateur(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", result);

        if (result.equals("Le collaborateur a été activé avec succès.")){
            return ResponseEntity.ok(response);
        } else if (result.equals("Collaborateur non trouvé.")) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}






}
