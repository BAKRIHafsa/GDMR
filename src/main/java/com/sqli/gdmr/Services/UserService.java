package com.sqli.gdmr.Services;

import com.sqli.gdmr.DTOs.CreateMedecinRequestDTO;
import com.sqli.gdmr.DTOs.ModifieCollaborateur;
import com.sqli.gdmr.DTOs.UserDTO;
import com.sqli.gdmr.Enums.Role;
import com.sqli.gdmr.Enums.UserStatus;
import com.sqli.gdmr.Mappers.ModifieCollaborateurMapper;
import com.sqli.gdmr.Models.Collaborateur;
import com.sqli.gdmr.Models.Medecin;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.CreneauRepository;
import com.sqli.gdmr.Repositories.MedecinRepository;
import com.sqli.gdmr.Repositories.UserRepository;
import jakarta.transaction.Transactional;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private CreneauRepository creneauRepository;

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

//    public User createUser(User user) {
//        String defaultPassword = "sqli";
//        String encodedPassword = passwordEncoder.encode(defaultPassword);
//
//        user.setPassword(encodedPassword);
//
//        user.setStatus(UserStatus.CREATED);
//
//        return userRepository.save(user);
//    }
public Collaborateur createCollaborateur(Collaborateur collaborateur) {
    String defaultPassword = "sqli";
    String encodedPassword = passwordEncoder.encode(defaultPassword);

    // Définir le mot de passe encodé pour le collaborateur
    collaborateur.setPassword(encodedPassword);

    // Définir le statut du collaborateur à 'CREATED'
    collaborateur.setStatus(UserStatus.CREATED);

    // Sauvegarder le collaborateur dans la base de données
    return userRepository.save(collaborateur);
}

    //    public String activerMedecina(Long id) {
//        Optional<User> userOpt = userRepository.findById(id);
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            if (user.getStatus() == UserStatus.CREATED) {
//                user.setStatus(UserStatus.ACTIVE);
//                userRepository.save(user);
//                return "Le médecin a été activé avec succès.";
//            } else {
//                return "Le médecin est déjà actif.";
//            }
//        } else {
//            return "Médecin non trouvé.";
//        }
//    }
public String activerMedecin(Long id) {
    // Rechercher l'utilisateur par son ID
    Optional<User> userOptional = userRepository.findById(id);
    if (!userOptional.isPresent()) {
        return "Médecin non trouvé.";
    }

    User user = userOptional.get();

    // Vérifier si l'utilisateur est un médecin
    if (!(user instanceof Medecin)) {
        return "L'utilisateur n'est pas un médecin.";
    }

    // Mettre à jour l'état de l'utilisateur en ACTIF
    user.setStatus(UserStatus.ACTIVE);

    // Sauvegarder les modifications
    userRepository.save(user);

    return "Le médecin a été activé avec succès.";
}
    public String activerCollaborateur(Long id) {
        // Rechercher l'utilisateur par son ID
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return "Collaborateur non trouvé.";
        }

        User user = userOptional.get();

        // Vérifier si l'utilisateur est un collaborateur ou un chargé RH
        if (!(user instanceof Collaborateur) && user.getRole() != Role.CHARGE_RH &&  user.getRole() != Role.ADMINISTRATEUR) {
            return "L'utilisateur n'est ni un collaborateur ni un chargé RH ni Administrateur.";
        }

        // Mettre à jour l'état de l'utilisateur en ACTIF
        user.setStatus(UserStatus.ACTIVE);

        // Sauvegarder les modifications
        userRepository.save(user);

        return "Le collaborateur a été activé avec succès.";
    }




    //    public String createUser(Long userId) {
//        if (userId == null) {
//            throw new IllegalArgumentException("User ID cannot be null");
//        }
//
//        Optional<User> existingUser = userRepository.findById(userId);
//
//        if (existingUser.isPresent()) {
//            User user = existingUser.get();
//            if (user.getStatus() == UserStatus.ACTIVE) {
//                return "User is already created";
//            } else {
//                user.setStatus(UserStatus.ACTIVE);
//                userRepository.save(user);
//
//                // Créer l'entrée dans la table Medecin
//                Medecin newMedecin = new Medecin();
//                newMedecin.setIdUser(user.getIdUser()); // Assurez-vous que votre Medecin a une relation avec User
//                newMedecin.setSpecialite("Médecin de travail"); // Définir la spécialité
//                medecinRepository.save(newMedecin); // Enregistrer le médecin
//
//                return "The user has been successfully created and assigned as a medecin";
//            }
//        } else {
//            User newUser = new User();
//            newUser.setIdUser(userId);
//            newUser.setStatus(UserStatus.ACTIVE);
//            userRepository.save(newUser);
//            // Créer l'entrée dans la table Medecin
//            Medecin newMedecin = new Medecin();
//            newMedecin.setIdUser(newUser.getIdUser()); // Assurez-vous que votre Medecin a une relation avec User
//            newMedecin.setSpecialite("Médecin de travail"); // Définir la spécialité
//            medecinRepository.save(newMedecin); // Enregistrer le médecin
//
//            return "The user has been successfully created and assigned as a médecin";
//        }
//    }
    public String createMedecinUser(CreateMedecinRequestDTO request) {
        // Vérifier les paramètres d'entrée
        if (request.getNom() == null || request.getPrenom() == null || request.getUsername() == null) {
            throw new IllegalArgumentException("Nom, prénom, et username ne peuvent pas être vides.");
        }

        // Vérifier si un utilisateur avec le même nom d'utilisateur existe déjà
        Optional<User> existingUserWithSameUsername = userRepository.findByUsername(request.getUsername());
        if (existingUserWithSameUsername.isPresent()) {
            throw new IllegalArgumentException("Le nom d'utilisateur existe déjà.");
        }

        // Créer un nouvel objet Medecin (qui hérite de User)
        Medecin medecin = new Medecin();
        medecin.setUsername(request.getUsername());
        medecin.setNom(request.getNom());
        medecin.setPrenom(request.getPrenom());
        medecin.setDateNaissance(request.getDateNaissance());
        medecin.setRole(Role.MEDECIN); // Définir le rôle de médecin
        medecin.setStatus(UserStatus.CREATED);

        // Définir le mot de passe par défaut encodé
        String encodedPassword = passwordEncoder.encode("sqli");
        medecin.setPassword(encodedPassword);

        // Définir les détails spécifiques au médecin
        medecin.setExperience(request.getExperience());
        medecin.setQualification(request.getQualification());
        medecin.setSiteTravail(request.getSiteTravail());
        medecin.setSpecialite("Médecin de travail"); // Valeur par défaut ou selon le request

        // Sauvegarder le médecin (cela sauvegarde aussi les détails utilisateur)
        medecinRepository.save(medecin);

        return "Le médecin a été créé avec succès";
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

//    public List<User> getAllCollaborateursA() {
//        List<User> collaborateurs = userRepository.findByRoleAndStatus(Role.COLLABORATEUR, UserStatus.ACTIVE);
//        List<User> administrateurs = userRepository.findByRoleAndStatus(Role.ADMINISTRATEUR, UserStatus.ACTIVE);
//        List<User> chargesRH = userRepository.findByRoleAndStatus(Role.CHARGE_RH, UserStatus.ACTIVE);
//
//        return Stream.of(collaborateurs, administrateurs, chargesRH)
//                .flatMap(List::stream)
//                .collect(Collectors.toList());
//    }

    public List<User> getAllCollaborateursA(LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        List<User> collaborateurs = userRepository.findByRoleAndStatus(Role.COLLABORATEUR, UserStatus.ACTIVE);
        List<User> administrateurs = userRepository.findByRoleAndStatus(Role.ADMINISTRATEUR, UserStatus.ACTIVE);
        List<User> chargesRH = userRepository.findByRoleAndStatus(Role.CHARGE_RH, UserStatus.ACTIVE);

        List<User> allUsers = Stream.of(collaborateurs, administrateurs, chargesRH)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return allUsers.stream()
                .filter(user -> !hasVisiteInCreneau(user, date, heureDebut, heureFin))
                .collect(Collectors.toList());
    }

    private boolean hasVisiteInCreneau(User user, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        return creneauRepository.existsByCollaborateurAndDateAndHeureDebutVisiteLessThanEqualAndHeureFinVisiteGreaterThanEqual(
                user, date, heureFin, heureDebut);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Collaborateur findByIdCollab(Long collaborateurId) {
        // Rechercher l'utilisateur par son ID
        return userRepository.findById(collaborateurId)
                .filter(user -> user instanceof Collaborateur) // Vérifier si l'utilisateur est un collaborateur
                .map(user -> (Collaborateur) user) // Cast l'utilisateur en Collaborateur
                .orElseThrow(() -> new IllegalArgumentException("Collaborateur non trouvé avec l'ID: " + collaborateurId));
    }

    public User findByIdChargeRh(Long chargeRhId) {
        // Rechercher l'utilisateur par son ID
        return userRepository.findById(chargeRhId)
                .filter(user -> user.getRole()==Role.CHARGE_RH) // Vérifier si l'utilisateur est un collaborateur
                .orElseThrow(() -> new IllegalArgumentException("Collaborateur non trouvé avec l'ID: " + chargeRhId));
    }

    public List<Medecin> findMedecinsDisponibles(LocalDate date, LocalTime heuredebut, LocalTime heurefin) {
        return medecinRepository.findAvailableMedecins(date, heuredebut, heurefin);
    }

}