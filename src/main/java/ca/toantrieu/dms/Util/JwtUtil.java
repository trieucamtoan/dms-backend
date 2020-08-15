package ca.toantrieu.dms.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtUtil {

    private Clock clock = DefaultClock.INSTANCE;
    private String secret = "$932k234kjjd43k4";

    @Value("${jwt.token.expiration.in.seconds}")
    private Long expiration;
    //Get username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Get issued date
    public Date extractDateForm(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    //Get expired date
    public Date extractDateExpired(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Get claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //Get all claims from token
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = extractDateExpired(token);
        return expiration.before(clock.now());
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }
    //Extend the expiration for the next 10 hours
    public String refreshToken(String token){
        final Date createdDate = new Date(System.currentTimeMillis());
        final Date expirationDate = calculateExpirationDate(createdDate);
        final Claims claims = extractAllClaims(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    //expire for the next 10 hours
    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration* 1000);
    }
}