package app.component;

import app.model.UserAuth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtComponent {
    private final LocalDateTime now = LocalDateTime.now();
    @Value("${jwt.secret.access}") String jwtAccessSecret;
    @Value("${jwt.secret.refresh}") String jwtRefreshSecret;
    private Key accessSecret;
    private Key refreshSecret;


    public String createtAccessToken(UserAuth userAuth){

        Instant access = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        Date maxdate = Date.from(access);
        accessSecret  = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));

        String token = Jwts.builder()
                .setSubject(userAuth.getUsername())
                .setExpiration(maxdate)
                .signWith(accessSecret)
                .claim("id",userAuth.getId())
                .compact();
        return token;
    }

    public String createRefreshToken(){
        Instant access = now.plusDays(10).atZone(ZoneId.systemDefault()).toInstant();
        Date maxdate = Date.from(access);
        refreshSecret  = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));

        String token = Jwts.builder()

                .setExpiration(maxdate)
                .signWith(refreshSecret)

                .compact();
        return token;
    }

    public boolean validateAccessToken(String accessToken){
        return validate(accessToken,accessSecret);
    }

    public boolean validateRefreshToken(String refreshToken){
        return validate(refreshToken,refreshSecret);
    }

    private boolean validate(String token,Key secret){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        catch (ExpiredJwtException expEx){
            expEx.getMessage();
        }
        return false;
    }

    public Claims getBodyAccessToken(String accessToken){
        return getBodyToken(accessToken,accessSecret);
    }

    public Claims getBodyRefreshToken(String refreshToken){
        return getBodyToken(refreshToken,refreshSecret);
    }

    private Claims getBodyToken(String token, Key secret){
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
