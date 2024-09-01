package com.unicam.Security;

import com.unicam.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@Component
public class JwtTokenProvider {

    private final String SECRET_JWT = "passwordSegretaAbbastanzaLunga1234ProgettoIngegneriaDelSoftware";
    private final Long JWT_EXPIRATION_TIME = 3600L;

    public String createToken(User user) {
        Claims claims = Jwts.claims();
        claims.put("name", user.getName());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRuolo());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_JWT)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_JWT)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getUserRole(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_JWT)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_JWT).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // Log and handle exceptions
        }
        return false;
    }


}
