package com.sqli.gdmr.Controllers;

//import com.sqli.gdmr.Services.AuthenticationService;
import com.sqli.gdmr.Models.LoginRequest;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private UserRepository userRepository;

@PostMapping("/auth/login")
public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
    String username = loginRequest.getUsername();
    String password = loginRequest.getPassword();

    //System.out.println("user: " + username + " password: " + password);

    org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
    );

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

    Instant instant = Instant.now();
    String scope = authentication.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .collect(Collectors.joining(" "));

    JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
            .issuedAt(instant)
            .expiresAt(instant.plus(60, ChronoUnit.MINUTES))
            .subject(username)
            .claim("scope", scope)
            .claim("userId", user.getIdUser())
            .build();

    JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
            JwsHeader.with(MacAlgorithm.HS512).build(),
            jwtClaimsSet
    );

    String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

    return Map.of("access_token", jwt);
}

}