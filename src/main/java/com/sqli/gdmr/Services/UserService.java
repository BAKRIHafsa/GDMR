package com.sqli.gdmr.Services;

import com.sqli.gdmr.DTOs.ModifieCollaborateur;
import com.sqli.gdmr.DTOs.UserDTO;
import com.sqli.gdmr.Enums.Role;
import com.sqli.gdmr.Enums.UserStatus;
import com.sqli.gdmr.Mappers.ModifieCollaborateurMapper;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;



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

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long userId = Long.valueOf(jwt.getClaim("userId").toString());
            String scope = jwt.getClaim("scope");
            Role role = Role.valueOf(scope.toUpperCase());

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

            user.setRole(role);

            return user;
        }
        return null;
    }

    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            Object scopeClaim = jwt.getClaim("scope");

            if (scopeClaim != null) {
                return scopeClaim.toString();
            }
        }

        return null;
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
//    public Map<String, List<User>> getActiveCollaborateurs() {
//        List<User> allUsers = userRepository.findAll();
//
//        Map<String, List<User>> usersByStatus = new HashMap<>();
//        usersByStatus.put("active", new ArrayList<>());
//
//        for (User user : allUsers) {
//            if ((user.getRole() == Role.COLLABORATEUR || user.getRole() == Role.CHARGE_RH || user.getRole() == Role.ADMINISTRATEUR) && (user.getStatus() == UserStatus.ACTIVE)){
//                    usersByStatus.get("active").add(user);
//            }
//        }
//
//        return usersByStatus;
//    }

    public Map<String, List<User>> getActiveCollaborateurs() {
        List<User> allUsers = userRepository.findAll();
        User currentUser = getCurrentUser();

        List<User> activeUsers = allUsers.stream()
                .filter(user ->
                        (user.getRole() == Role.COLLABORATEUR ||
                                user.getRole() == Role.CHARGE_RH ||
                                user.getRole() == Role.ADMINISTRATEUR) &&
                                user.getStatus() == UserStatus.ACTIVE &&
                                (currentUser == null || !user.getIdUser().equals(currentUser.getIdUser())) // Exclude current user if authenticated
                )
                .collect(Collectors.toList());


        Map<String, List<User>> usersByStatus = new HashMap<>();
        usersByStatus.put("active", activeUsers);

        return usersByStatus;
    }


//    public Map<String, List<User>> getActiveCollaborateurs() {
//        User currentUser = getCurrentUser();
//        List<User> activeUsers;
//
//        if (currentUser != null) {
//            // Retrieve only active users with the relevant roles and exclude the current user
//            activeUsers = userRepository.findByStatusAndRoleInAndNotId(
//                    UserStatus.ACTIVE,
//                    List.of(Role.COLLABORATEUR, Role.CHARGE_RH, Role.ADMINISTRATEUR),
//                    currentUser.getIdUser()
//            );
//        } else {
//            // Handle the case where there is no authenticated user
//            activeUsers = userRepository.findByStatusAndRoleIn(
//                    UserStatus.ACTIVE,
//                    List.of(Role.COLLABORATEUR, Role.CHARGE_RH, Role.ADMINISTRATEUR)
//            );
//        }
//
//        Map<String, List<User>> usersByStatus = new HashMap<>();
//        usersByStatus.put("active", activeUsers);
//
//        return usersByStatus;
//    }

//    public Map<String, List<User>> getActiveCollaborateurs() {
//        User currentUser = getCurrentUser();
//        List<User> allUsers = userRepository.findAll();
//
//        List<User> activeUsers = allUsers.stream()
//                .filter(user ->
//                        (user.getRole() == Role.COLLABORATEUR || user.getRole() == Role.CHARGE_RH || user.getRole() == Role.ADMINISTRATEUR) && user.getStatus() == UserStatus.ACTIVE &&
//                                (currentUser == null || !user.equals(currentUser)) // Exclude current user if authenticated
//                )
//                .collect(Collectors.toList());
//
//        Map<String, List<User>> usersByStatus = new HashMap<>();
//        usersByStatus.put("active", activeUsers);
//
//        return usersByStatus;
//    }

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

    public Map<String, List<User>> getActiveMedecins() {
        List<User> allUsers = userRepository.findAll();

        Map<String, List<User>> usersByStatus = new HashMap<>();
        usersByStatus.put("active", new ArrayList<>());

        for (User user : allUsers) {
            if ((user.getRole() == Role.MEDECIN) && (user.getStatus() == UserStatus.ACTIVE)) {
                    usersByStatus.get("active").add(user);
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

//    public User createUser(UserDTO userCreationDTO) {
//        User user = new User();
//        user.setNom(userCreationDTO.getNom());
//        user.setPrenom(userCreationDTO.getPrenom());
//        user.setUsername(userCreationDTO.getUsername());
//        user.setDateNaissance(userCreationDTO.getDateNaissance());
//        user.setRole(userCreationDTO.getRole());
//        user.setStatus(UserStatus.CREATED);
//
//        return userRepository.save(user);
//    }

    public User createUser(User user) {
        String defaultPassword = "sqli";
        String encodedPassword = passwordEncoder.encode(defaultPassword);

        user.setPassword(encodedPassword);

        user.setStatus(UserStatus.CREATED);

        return userRepository.save(user);
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

    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public List<User> getAllCollaborateurs() {
        return userRepository.findByRole(Role.COLLABORATEUR);
    }

}