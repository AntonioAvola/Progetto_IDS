package com.unicam.Security;

import com.unicam.Hadlers.CustomAccessNegatoHandler;
import com.unicam.Service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
            "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/api/auth/**",
            "/api/test/**", "/authenticate", "/api/utente/Api/RegistrazioneUtente", "/Api/GestorePiattaforma/Registrazione" };

    @Autowired
    private final JwtRequestFilter jwtRequestFilter;
    @Autowired
    private final UtenteService servizioUtente;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UtenteService servizioUtente) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.servizioUtente = servizioUtente;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Disabilita CSRF per le API stateless
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(WHITE_LIST_URL).permitAll()  // Permetti l'accesso senza autenticazione al login
                        .anyRequest().authenticated()               // Richiedi autenticazione per tutte le altre richieste

                ).exceptionHandling(exceptionHandling -> exceptionHandling
                                .accessDeniedHandler(new CustomAccessNegatoHandler())
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Aggiungi il filtro JWT

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
