package com.sqli.gdmr.Security;


import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.sqli.gdmr.Services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String secretKey;

    //    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        PasswordEncoder passwordEncoder = passwordEncoder();
//        return new InMemoryUserDetailsManager(
//                 User.withUsername("user1").password(passwordEncoder.encode("12345")).authorities("USER").build(),
//                User.withUsername("ADMIN").password(passwordEncoder.encode("12345")).authorities("USER", "ADMIN ").build()
//        );
//    }

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity
                .cors(Customizer.withDefaults()) // Ajoutez cette ligne pour activer CORS
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login/**").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ADMINISTRATEUR")//permitAll()
                        .requestMatchers("/api/chargéRH/**").hasAuthority("CHARGE_RH")//permitAll()
                        .requestMatchers("/api/collab/**").hasAnyAuthority("COLLABORATEUR", "CHARGE_RH", "ADMINISTRATEUR")
                        .requestMatchers("/api/med/**").hasAuthority("MEDECIN")
                        .requestMatchers("/api/disponibilites/medecin/**").hasAnyAuthority("CHARGE_RH", "MEDECIN")
                        .requestMatchers("/api/creneaux/**").hasAuthority("CHARGE_RH")
                        .anyRequest().authenticated()
                )
                //.httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oa->oa.jwt(Customizer.withDefaults()))
                .userDetailsService(userDetailsService)
                .build();
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    //va générer la signature de token, va encoder
    //mode symétrique donc un seul code privé
    //il ya mode asymétrique qui a besoin de deux codes un privé et l'autre public
    @Bean
    JwtEncoder jwtEncoder(){
        //String secretKey="9faa372517ac1d389758d3750fc87acf00f542277f26fec1ce4593e93f64e338";
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
    }
    // chaque fois qu'il recoit une requete, il va vers la requete http il cherche le header authorization, il récupère le jwt après il va vérifier la signature de token
    @Bean
    JwtDecoder jwtDecoder(){
        //String secretKey="9faa372517ac1d389758d3750fc87acf00f542277f26fec1ce4593e93f64e338";
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }

    //    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        return new ProviderManager(daoAuthenticationProvider);
//    }
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

}
