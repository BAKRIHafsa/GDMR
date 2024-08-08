package com.sqli.gdmr.Services;

import com.sqli.gdmr.DTOs.ModifieCollaborateur;
import com.sqli.gdmr.Enums.Role;
import com.sqli.gdmr.Enums.UserStatus;
import com.sqli.gdmr.Mappers.ModifieCollaborateurMapper;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

//    public User getUserById(Long id) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
//    }

//    public Map<String, List<User>> getAllUsersByStatus() {
//        List<User> allUsers = userRepository.findAll();
//
//        Map<String, List<User>> usersByStatus = new HashMap<>();
//        usersByStatus.put("created", new ArrayList<>());
//        usersByStatus.put("active", new ArrayList<>());
//        usersByStatus.put("archived", new ArrayList<>());
//
//        for (User user : allUsers) {
//            if (user.getRole() == Role.ADMINISTRATEUR && user.getStatus() == UserStatus.ACTIVE) {
//                continue;
//            }
//            switch (user.getStatus()) {
//                case CREATED:
//                    usersByStatus.get("created").add(user);
//                    break;
//                case ACTIVE:
//                    usersByStatus.get("active").add(user);
//                    break;
//                case ARCHIVED:
//                    usersByStatus.get("archived").add(user);
//                    break;
//            }
//        }
//
//        return usersByStatus;
//    }
public Map<String, List<User>> getActiveAndCreatedCollaborateursAndChargeRH() {
    List<User> allUsers = userRepository.findAll();

    Map<String, List<User>> usersByStatus = new HashMap<>();
    usersByStatus.put("created", new ArrayList<>());
    usersByStatus.put("active", new ArrayList<>());

    for (User user : allUsers) {
        if ((user.getRole() == Role.COLLABORATEUR || user.getRole() == Role.CHARGE_RH) &&
                (user.getStatus() == UserStatus.CREATED || user.getStatus() == UserStatus.ACTIVE)) {
            if (user.getStatus() == UserStatus.CREATED) {
                usersByStatus.get("created").add(user);
            } else if (user.getStatus() == UserStatus.ACTIVE) {
                usersByStatus.get("active").add(user);
            }
        }
    }

    return usersByStatus;
}

    public List<User> getArchivedCollaborateursAndChargeRH() {
        List<User> allUsers = userRepository.findAll();

        List<User> archivedUsers = new ArrayList<>();

        for (User user : allUsers) {
            if ((user.getRole() == Role.COLLABORATEUR || user.getRole() == Role.CHARGE_RH) &&
                    user.getStatus() == UserStatus.ARCHIVED) {
                archivedUsers.add(user);
            }
        }

        return archivedUsers;
    }
    public Map<String, List<User>> getActiveAndCreatedMedecins() {
        List<User> allUsers = userRepository.findAll();

        Map<String, List<User>> usersByStatus = new HashMap<>();
        usersByStatus.put("created", new ArrayList<>());
        usersByStatus.put("active", new ArrayList<>());

        for (User user : allUsers) {
            if ((user.getRole() == Role.MEDECIN) &&
                    (user.getStatus() == UserStatus.CREATED || user.getStatus() == UserStatus.ACTIVE)) {
                if (user.getStatus() == UserStatus.CREATED) {
                    usersByStatus.get("created").add(user);
                } else if (user.getStatus() == UserStatus.ACTIVE) {
                    usersByStatus.get("active").add(user);
                }
            }
        }

        return usersByStatus;
    }

    public List<User> getArchivedMedecins() {
        List<User> allUsers = userRepository.findAll();

        List<User> archivedUsers = new ArrayList<>();

        for (User user : allUsers) {
            if ((user.getRole() == Role.MEDECIN ) &&
                    user.getStatus() == UserStatus.ARCHIVED) {
                archivedUsers.add(user);
            }
        }

        return archivedUsers;
    }

    public String createUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        Optional<User> existingUser = userRepository.findById(userId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getStatus() == UserStatus.ACTIVE) {
                return "User is already created";
            } else{
                user.setStatus(UserStatus.ACTIVE);
                userRepository.save(user);
                return "The user has been successfully created";
            }
        } else {
            User newUser = new User();
            newUser.setIdUser(userId);
            newUser.setStatus(UserStatus.ACTIVE);
            userRepository.save(newUser);
            return "The user has been successfully created";
        }
    }

//    public User updateUser(Long id, User userDetails) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
//
//        if (userDetails == null) {
//            throw new IllegalArgumentException("User details cannot be null");
//        }
//
//        if (userDetails.getUsername() != null) {
//            user.setUsername(userDetails.getUsername());
//        }
//        if (userDetails.getNom() != null) {
//            user.setNom(userDetails.getNom());
//        }
//        if (userDetails.getPrenom() != null) {
//            user.setPrenom(userDetails.getPrenom());
//        }
//        if (userDetails.getRole() != null) {
//            user.setRole(userDetails.getRole());
//        }
//        if (userDetails.getDateNaissance() != null) {
//            user.setDateNaissance(userDetails.getDateNaissance());
//        }
//
//        return userRepository.save(user);
//    }

    public User updateUser(Long id, ModifieCollaborateur dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        ModifieCollaborateurMapper.updateUserFromDto(user, dto);
        return userRepository.save(user);
    }

    public User archiveUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));

        user.setStatus(UserStatus.ARCHIVED);
        return userRepository.save(user);
    }
}