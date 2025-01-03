package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.DTOs.ChangePasswordDTO;
import com.sqli.gdmr.DTOs.ChangePasswordRequestDTO;
import com.sqli.gdmr.Enums.Role;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.DTOs.UserDTO;
import com.sqli.gdmr.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/change-password-premier-fois")
    @PreAuthorize("isAuthenticated()") // Vérifie si l'utilisateur est authentifié
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        try {
            userService.changePassword(request);
            return ResponseEntity.ok("Mot de passe changé avec succès"); // Réponse de succès
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Utilisateur non trouvé
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du changement de mot de passe"); // Erreur générique
        }
    }


    @GetMapping("/profile")
    public User getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            String username = jwtToken.getName(); // Ceci récupère généralement le 'sub' claim du JWT

            User user = userService.findByUsername(username);
            if (user != null) {
                return new User(user.getNom(), user.getPrenom(), user.getUsername(),user.getDateNaissance(), user.getRole());
            }
        }

        throw new RuntimeException("Utilisateur non authentifié ou introuvable");
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO request) {
        try {
            // Get the username from the authentication context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof JwtAuthenticationToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié");
            }
            String username = ((JwtAuthenticationToken) authentication).getName();

            // Perform the password change
            userService.changePassword(username, request.getCurrentPassword(), request.getNewPassword());

            return ResponseEntity.ok("Mot de passe changé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/role")
    public ResponseEntity<Map<String, String>> getCurrentUserRole() {
        String role = userService.getCurrentUserRole();
        if (role != null) {
            Map<String, String> response = new HashMap<>();
            response.put("role", role);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/current-med-id")
    public ResponseEntity<Long> getCurrentMedecinId() {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null && Role.MEDECIN.equals(currentUser.getRole())) {
            return ResponseEntity.ok(currentUser.getIdUser());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @GetMapping("/all-collab")
    public ResponseEntity<List<User>> getAllCollaborateursA() {
        List<User> collaborateurs = userService.getAllCollaborateursA();
        return ResponseEntity.ok(collaborateurs);
    }
@GetMapping("/all-collabAV")
public ResponseEntity<List<User>> getAllCollaborateursAV(
        @RequestParam LocalDate date,
        @RequestParam LocalTime heureDebut,
        @RequestParam LocalTime heureFin) {
    // Appel au service avec les paramètres de date et heures
    List<User> collaborateurs = userService.getAllCollaborateursAV(date, heureDebut, heureFin);
    return ResponseEntity.ok(collaborateurs);
}


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
