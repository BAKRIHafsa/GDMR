package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.Models.Notification;
import com.sqli.gdmr.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

