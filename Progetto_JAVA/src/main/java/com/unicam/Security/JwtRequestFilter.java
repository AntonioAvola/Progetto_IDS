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
    private JwtTokenProvider jwtTokenProvider;

    private String SALT = "passwordSegretaAbbastanzaLunga1234ProgettoIngegneriaDelSoftware";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);  // Rimuovi "Bearer "

            try {
                if (jwtToken.contains(" ")) {
                    logger.error("Token contiene spazi non validi");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non valido");
                    return;
                }

                Claims claims = Jwts.parser()
                        .setSigningKey(SALT)
                        .parseClaimsJws(jwtToken)
                        .getBody();

                String username = claims.get("username").toString();
                String userId = claims.get("id").toString();
                String role = claims.get("role").toString();
                String comune = claims.get("comune").toString();

                // Crea una lista di authority
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(role));


                UserCustomDetails userCustomDetails = new UserCustomDetails(username, userId, role, comune);

                logger.info("utente autenticato: " + username + "con ruolo: " + role + "e id: " + userId);


                // Imposta l'autenticazione nel contesto di sicurezza
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userCustomDetails, null, userCustomDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException | IllegalArgumentException e) {
                logger.error("Errore nella decodifica del token JWT", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non valido");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
