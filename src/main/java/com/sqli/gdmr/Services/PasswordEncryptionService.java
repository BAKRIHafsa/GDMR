package com.sqli.gdmr.Services;

import java.util.List;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncryptionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void encryptPasswords() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            String currentPassword = user.getPassword();

            // Vérifiez si le mot de passe commence par le préfixe BCrypt
            if (!currentPassword.startsWith("$2a$") && !currentPassword.startsWith("$2b$") && !currentPassword.startsWith("$2y$")) {
                String encryptedPassword = passwordEncoder.encode(currentPassword);
                user.setPassword(encryptedPassword);
                userRepository.save(user);
                //System.out.println("Mot de passe crypté pour l'utilisateur: " + user.getUsername());
            } else {
                //System.out.println("Le mot de passe pour l'utilisateur " + user.getUsername() + " est déjà crypté.");
            }
        }
    }
}
