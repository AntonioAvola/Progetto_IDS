package com.unicam.Security;

import com.unicam.Service.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.crypto.Cipher.SECRET_KEY;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;  // Usa il provider per gestire la logica del token

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Ottieni il token dall'header Authorization
        String jwtToken = jwtTokenProvider.resolveToken(request);

        if (jwtToken != null) {
            try {
                // Estrai i claims dal token
                Claims claims = jwtTokenProvider.extractAllClaims(jwtToken);
                String username = claims.get("username", String.class);
                String role = claims.get("role", String.class);
                String comune = claims.get("comune", String.class);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Carica i dettagli dell'utente
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

                    if (jwtTokenProvider.validateToken(jwtToken, userDetails.getUsername())) {
                        // Costruisci l'oggetto UserCustomDetails o utilizza i dettagli utente standard
                        UserCustomDetails userCustomDetails = new UserCustomDetails(username, claims.get("id").toString(), role, comune);

                        // Imposta le authorities per l'utente
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(new SimpleGrantedAuthority(role));

                        // Setta il contesto di sicurezza
                        setAuthentication(userCustomDetails, authorities, request);
                    }
                }
            } catch (ExpiredJwtException e) {
                logger.warn("Token JWT scaduto", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT scaduto");
                return;
            } catch (JwtException | IllegalArgumentException e) {
                logger.error("Errore nel token JWT", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT non valido");
                return;
            }
        }

        // Continua con la catena di filtri
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(UserCustomDetails userCustomDetails, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userCustomDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }



}
