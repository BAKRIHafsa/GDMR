package com.sqli.gdmr.Services;

import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Enums.UserStatus;
import com.sqli.gdmr.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UsernameNotFoundException("User is not active");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        String roleName = user.getRole().name();
        authorities.add(new SimpleGrantedAuthority(roleName));

        System.out.println("User role: " + roleName);  // Debug log
        System.out.println("Authorities: " + authorities);  // Debug log
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                //.authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())))
                //.authorities(Collections.singletonList(new SimpleGrantedAuthority("ADMINISTRATEUR")))
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
