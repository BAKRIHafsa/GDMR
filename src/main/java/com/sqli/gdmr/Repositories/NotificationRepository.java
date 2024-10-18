package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Models.Creneau;
import com.sqli.gdmr.Models.Notification;
import com.sqli.gdmr.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Long countByDestinataireIdUserAndLuFalse(Long destinataireIdUser);

    List<Notification> findByDestinataireIdUserAndLuFalse(Long userId);

    @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END " +
            "FROM Notification n WHERE n.creneau = :creneau AND n.daysBefore = :daysBefore")
    boolean existsByCreneauAndDaysBefore(@Param("creneau") Creneau creneau, @Param("daysBefore") int daysBefore);

    @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END " +
            "FROM Notification n WHERE n.creneau = :creneau AND n.daysBefore = :daysBefore AND n.destinataire = :destinataire")
    boolean existsByCreneauAndDaysBeforeAndDestinataire(@Param("creneau") Creneau creneau, @Param("daysBefore") int daysBefore, @Param("destinataire") User destinataire);

    List<Notification> findByDestinataireIdUser(Long userId);

}
