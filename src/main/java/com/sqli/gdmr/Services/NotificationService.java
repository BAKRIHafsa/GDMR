package com.sqli.gdmr.Services;

import com.sqli.gdmr.Models.Notification;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public Long getUnreadNotificationsCount() {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            Long userId = currentUser.getIdUser();
            return notificationRepository.countByDestinataireIdUserAndLuFalse(userId);
        }
        throw new IllegalStateException("Utilisateur non authentifié ou non trouvé.");
    }

    public List<Notification> getAllNotificationsForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return notificationRepository.findByDestinataireIdUser(currentUser.getIdUser());
    }

    public void markAsRead(Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setLu(true);
            notificationRepository.save(notification);
        }
    }
    public void sendNotification(User destinataire, String message) {
        Notification notification = new Notification();
        notification.setDestinataire(destinataire);
        notification.setMessage(message);
        notification.setDateEnvoi(LocalDateTime.now());
        notificationRepository.save(notification);
    }

//    public void sendNotificationsToCollaborateurs(List<CreneauCollaborateur> creneauCollaborateurs) {
//        for (CreneauCollaborateur cc : creneauCollaborateurs) {
//            // Logic to send a notification to the collaborator
//            System.out.println("Notification sent to Collaborateur ID: " + cc.getCollaborateur().getIdUser());
//            // Implement the actual notification logic (e.g., email, SMS, push notification)
//        }
//    }
}
