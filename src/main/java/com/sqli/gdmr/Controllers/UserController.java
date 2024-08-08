package com.sqli.gdmr.Controllers;

import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.DTOs.UserDTO;
import com.sqli.gdmr.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/profile")
    public UserDTO getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            String username = jwtToken.getName(); // Ceci récupère généralement le 'sub' claim du JWT

            User user = userService.findByUsername(username);
            if (user != null) {
                return new UserDTO(user.getNom(), user.getPrenom());
            }
        }

        throw new RuntimeException("Utilisateur non authentifié ou introuvable");
    }
}
