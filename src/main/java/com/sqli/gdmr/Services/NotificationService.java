package com.sqli.gdmr.Services;

import com.sqli.gdmr.Models.CreneauCollaborateur;
import com.sqli.gdmr.Models.Notification;
import com.sqli.gdmr.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public Long getUnreadNotificationsCount(Long userId) {
        return notificationRepository.countByDestinataireIdUserAndLuFalse(userId);
    }

    public void markAsRead(Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setLu(true);
            notificationRepository.save(notification);
        }
    }
    public void sendNotificationsToCollaborateurs(List<CreneauCollaborateur> creneauCollaborateurs) {
        for (CreneauCollaborateur cc : creneauCollaborateurs) {
            // Logic to send a notification to the collaborator
            System.out.println("Notification sent to Collaborateur ID: " + cc.getCollaborateur().getIdUser());
            // Implement the actual notification logic (e.g., email, SMS, push notification)
        }
    }
}
