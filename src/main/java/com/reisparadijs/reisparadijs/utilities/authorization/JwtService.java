package com.reisparadijs.reisparadijs.utilities.authorization;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 13:29
 */

@Service
public class JwtService {

    @Value("${app.secret}")
    private   String appSecret;

    @Value("${app.jwt.token.expires-in}")
    private  Long tokenExpiresIn;

    @Value("${app.jwt.refresh-token.expires-in}")
    private Long refreshTokenExpiresIn;

    @Value("${app.jwt.remember-me.expires-in}")
    private   Long rememberMeTokenExpiresIn;



    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
 public String extractPassword(String token) {
        return extractClaim(token, claims -> (String) claims.get("password"));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, AppUser userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUserName()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(appSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Builds a JWT token based on the provided extra claims, user details, and expiration time.
     *
     * @param extraClaims  a map of additional claims to be included in the token
     * @param userDetails  the user details to be used in the token
     * @param expiration   the expiration time of the token in milliseconds
     * @return             the generated JWT token
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            AppUser userDetails,
            long expiration) {
        var authorities = userDetails.getRoles()
                .stream().map(Role::getName)
                .toList();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .claim("authorities", authorities)
                .setSubject(userDetails.getUserName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(getExpireDate(expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * Generates an access token based on the provided extra claims and user details.
     *
     * @param  extraClaims   the extra claims to include in the token
     * @param  userDetails    the user details for whom the token is generated
     * @return               the generated access token
     */
    public String generateAccessToken(
            Map<String, Object> extraClaims,
            AppUser userDetails) {
        return buildToken(extraClaims, userDetails, tokenExpiresIn);
    }

    /**
     * Generates a refresh token for the given user details.
     *
     * @param userDetails the user details to generate the refresh token for
     * @return the generated refresh token
     */
    public String generateRefreshToken(
            Map<String, Object> extraClaims,AppUser userDetails) {
        extraClaims.put("password", userDetails.getPassword());
        return buildToken(extraClaims, userDetails, refreshTokenExpiresIn);
    }

    /**
     * Set jwt refresh token for remember me option.
     */
    public void setRememberMe() {
        this.refreshTokenExpiresIn = rememberMeTokenExpiresIn;
    }

    /**
     * Get expire date.
     *
     * @return Date object
     */
    private Date getExpireDate(final Long expires) {
        return new Date(new Date().getTime() + expires);
    }



}
