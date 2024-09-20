package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.DTOs.ModifieCollaborateur;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> createUser(@RequestBody Map<String, Long> requestBody) {
        Long userId = requestBody.get("idUser");
        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }
        String createdUser = userService.createUser(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

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

    @PostMapping("/create")
    public User createUser(@RequestBody User userCreation) {
        return userService.createUser(userCreation);
    }
}
