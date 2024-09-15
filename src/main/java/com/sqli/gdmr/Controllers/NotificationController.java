package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/count-unread/{userId}")
    public ResponseEntity<Long> getUnreadNotificationsCount(@PathVariable Long userId) {
        Long count = notificationService.getUnreadNotificationsCount(userId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/mark-as-read")
    public ResponseEntity<Void> markAsRead(@RequestBody Map<String, Long> payload) {
        Long notificationId = payload.get("notificationId");
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
}

