package app.component;

import app.model.UserAuth;
import com.auth0.jwt.exceptions.JWTDecodeException;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;


import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtComponent {

    private String jwtAccessSecret = "qBTmv4oXFFR2GwjexDJ4t6fsIUIUhhXqlktXjXdkcyygs8nPVEwMfo29VDRRepYDVV5IkIxBMzr7OEHXEHd37w==";
    private  String jwtRefreshSecret = "zL1HB3Pch05Avfynovxrf/kpF9O2m4NCWKJUjEp27s9J2jEG3ifiKCGylaZ8fDeoONSTJP/wAzKawB8F9rOMNg==";
    private Key accessSecret =  Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    private Key refreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));


    public String createtAccessToken(UserAuth userAuth){
        LocalDateTime now = LocalDateTime.now();
        Instant access = now.plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant();
        Date maxdate = Date.from(access);

        String token = Jwts.builder()
                .setSubject(userAuth.getUsername())
                .setExpiration(maxdate)
                .signWith(accessSecret)
                .claim("login",userAuth.getUsername())
                .compact();
        return token;
    }

    public String createRefreshToken(UserAuth userAuth){
        LocalDateTime now = LocalDateTime.now();
        Instant access = now.plusDays(10).atZone(ZoneId.systemDefault()).toInstant();
        Date maxdate = Date.from(access);

        String token = Jwts.builder()

                .claim("login",userAuth.getUsername())
                .setExpiration(maxdate)
                .signWith(refreshSecret)
                .compact();
        return token;
    }

    public boolean validateAccessToken(String accessToken){

        System.out.println(accessToken);
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
        catch (JWTDecodeException expEx){
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
