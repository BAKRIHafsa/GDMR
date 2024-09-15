package com.sqli.gdmr;

import com.sqli.gdmr.Services.PasswordEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptionRunner implements CommandLineRunner {

    @Autowired
    private PasswordEncryptionService passwordEncryptionService;

    @Override
    public void run(String... args) throws Exception {
        passwordEncryptionService.encryptPasswords();
        System.out.println("Tous les mots de passe ont été cryptés.");
    }
}
