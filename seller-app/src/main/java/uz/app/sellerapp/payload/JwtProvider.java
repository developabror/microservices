package uz.app.sellerapp.payload;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {


    @Value("${jwt.signatureAlgorithm}")
    private String key;


    private Long expireTimeout=86000000l;

    public String generateToken(String  username){
        Date date =new Date(System.currentTimeMillis()+expireTimeout);
        return Jwts
                .builder()
                .setIssuedAt(new Date())
                .setSubject(username)
                .setExpiration(date)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token){
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public Claims getClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Key getKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }
}
