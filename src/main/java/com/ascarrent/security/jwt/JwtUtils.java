package com.ascarrent.security.jwt;

import com.ascarrent.exception.message.ErrorMessages;
import com.ascarrent.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);


    @Value("${ascarrent.app.jwtSecret}")
    private String jwtSecret;
    @Value("${ascarrent.app.jwtExpirationMs}")
    private Long jwtExpirationMs;

    // -- Generate JWT Token
    public String generateJwtToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // -- Get E-mail (username) in jwtToken
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // -- JWT Validate
    public Boolean validateJwtToken(String token) {

        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret) // use jwtSecret key to sign with
                    .parseClaimsJws(token); // header and payload in jwtToken
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            logger.error(String.format(
                    ErrorMessages.JWTTOKEN_ERROR_MESSAGE, e.getMessage()));
            return false;
        }
    }

}
