package com.sqli.gdmr.Services;

import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Models.Notification;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.CreneauRepository;
import com.sqli.gdmr.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CreneauRepository creneauRepository;


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
        notification.setLu(false); // Initialement, la notification n'est pas lue
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
    public void sendReminder(User recipient, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setDestinataire(recipient);
        notification.setDateEnvoi(LocalDateTime.now());
        notification.setLu(false);

        notificationRepository.save(notification);

        System.out.println("Notification sauvegardée pour l'utilisateur " + recipient.getUsername() + ": " + message);
    }
    //@Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(cron = "0 * * * * ?") // Planification pour des tests
    public void sendReminders() {
        LocalDate today = LocalDate.now();
        List<Creneau> creneaux = creneauRepository.findAll();

        for (Creneau creneau : creneaux) {
            LocalDate dateVisite = creneau.getDate();
            long daysUntilVisit = java.time.temporal.ChronoUnit.DAYS.between(today, dateVisite);

            // Vérifier le nombre de jours avant la visite
            if (daysUntilVisit == 7 && !hasReminderBeenSent(creneau, 7)) {
                // Notification pour le collaborateur
                sendReminder(creneau.getCollaborateur(), "Rappel : Votre visite est dans une semaine.", creneau, 7);
                // Notification pour le médecin
                sendReminder(creneau.getMedecin(), "Rappel : Vous avez une visite prévue dans une semaine.", creneau, 7);
            } else if (daysUntilVisit == 3 && !hasReminderBeenSent(creneau, 3)) {
                // Notification pour le collaborateur
                sendReminder(creneau.getCollaborateur(), "Rappel : Votre visite est dans 3 jours.", creneau, 3);
                // Notification pour le médecin
                sendReminder(creneau.getMedecin(), "Rappel : Vous avez une visite prévue dans 3 jours.", creneau, 3);
            } else if (daysUntilVisit == 1 && !hasReminderBeenSent(creneau, 1)) {
                // Notification pour le collaborateur
                sendReminder(creneau.getCollaborateur(), "Rappel : Votre visite est demain.", creneau, 1);
                // Notification pour le médecin
                sendReminder(creneau.getMedecin(), "Rappel : Vous avez une visite prévue demain.", creneau, 1);
            }
        }
    }

    private boolean hasReminderBeenSent(Creneau creneau, int daysBefore) {
        return notificationRepository.existsByCreneauAndDaysBefore(creneau, daysBefore);
    }
    private boolean hasReminderBeenSent(Creneau creneau, int daysBefore, User destinataire) {
        return notificationRepository.existsByCreneauAndDaysBeforeAndDestinataire(creneau, daysBefore, destinataire);
    }
    // Marque une notification comme envoyée pour éviter les doublons
    public void sendReminder(User recipient, String message, Creneau creneau, int daysBefore) {
        if (recipient != null) { // Vérifier que le destinataire n'est pas null
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDestinataire(recipient);
            notification.setCreneau(creneau);  // Associer le créneau ici
            notification.setDaysBefore(daysBefore);
            notification.setDateEnvoi(LocalDateTime.now());
            notification.setLu(false);

            notificationRepository.save(notification);

            System.out.println("Notification sauvegardée pour l'utilisateur " + recipient.getUsername() + ": " + message);
        }
    }

    public void markAllAsRead() {
        User currentUser = userService.getCurrentUser(); // Récupérer l'utilisateur connecté
        if (currentUser != null) {
            List<Notification> notifications = notificationRepository.findByDestinataireIdUserAndLuFalse(currentUser.getIdUser());
            for (Notification notification : notifications) {
                notification.setLu(true);
            }
            notificationRepository.saveAll(notifications);
        } else {
            throw new IllegalStateException("Utilisateur non connecté");
        }
    }
}
