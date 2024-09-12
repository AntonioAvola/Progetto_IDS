package com.unicam.Security;

import com.unicam.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;


@Component
public class JwtTokenProvider {

    private final String SECRET_JWT = "passwordSegretaAbbastanzaLunga1234ProgettoIngegneriaDelSoftware";
    private final Long JWT_EXPIRATION_TIME = 3600000L;

    public String createToken(User user) {
        Claims claims = Jwts.claims();
        claims.put("id", user.getId()); //new
        claims.put("name", user.getName());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRuoloComune());
        claims.put("comune", user.getComune());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_JWT)
                .compact();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_JWT)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_JWT)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("username", String.class);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String getUserRole(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_JWT)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }


    public Boolean validateToken(String token, String username) {
        final String extractedUsername = getUsernameFromJWT(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Rimuove "Bearer " e restituisce il token
        }
        return bearerToken;
    }




}
