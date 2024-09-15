package com.sqli.gdmr.Models;

import com.sqli.gdmr.Enums.Role;
import com.sqli.gdmr.Enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    @Column(unique = true)
    private String username;
    private String password;
    private String nom;
    private String prenom;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private UserStatus Status = UserStatus.CREATED;
    private LocalDate dateNaissance;
    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", username='" + username + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", role=" + role +
                ", status=" + Status +
                ", dateNaissance=" + dateNaissance +
                '}';
    }

    public User(String nom, String prenom, String username, LocalDate dateNaissance, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.dateNaissance = dateNaissance;
        this.role = role;
    }

    public User() {
    }
}
