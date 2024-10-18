package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.Models.Notification;
import com.sqli.gdmr.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/count-unread")
    public Long getUnreadNotificationsCount() {
        return notificationService.getUnreadNotificationsCount();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAllNotificationsForCurrentUser() {
        List<Notification> notifications = notificationService.getAllNotificationsForCurrentUser();
        return ResponseEntity.ok(notifications);
    }


    @PostMapping("/mark-as-read")
    public ResponseEntity<Void> markAsRead(@RequestBody Map<String, Long> payload) {
        Long notificationId = payload.get("notificationId");
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sendReminders")
    public ResponseEntity<String> sendReminders() {
        try {
            notificationService.sendReminders(); // Appelle la méthode dans le service qui gère les rappels
            return ResponseEntity.ok("Rappels envoyés avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'envoi des rappels.");
        }
    }

    @PutMapping("/markAllAsRead")
    public ResponseEntity<Map<String, String>> markAllAsRead() {
        try {
            notificationService.markAllAsRead();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Toutes les notifications ont été marquées comme lues.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erreur lors de la mise à jour des notifications."));
        }
    }

}

