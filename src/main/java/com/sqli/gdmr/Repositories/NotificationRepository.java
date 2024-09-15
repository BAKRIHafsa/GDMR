package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Long countByDestinataireIdUserAndLuFalse(Long destinataireIdUser);
}
