package com.sqli.gdmr.Repositories;

import com.sqli.gdmr.Enums.Role;
import com.sqli.gdmr.Enums.UserStatus;
import com.sqli.gdmr.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //List<User> findByStatus(UserStatus status);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long userId);
    List<User> findByRole(Role role);
}



