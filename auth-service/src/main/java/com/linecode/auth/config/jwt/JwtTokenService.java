package com.linecode.auth.config.jwt;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.linecode.auth.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenService {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CLAIMS_RULES_PROP = "rules";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-timeout}")
    private long expireTimeout;

    @Autowired
    private UserService userService;

    public String getToken(HttpServletRequest request) {
        
        var bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        
        if (isBearerToken(bearerToken)) {
            return bearerTokenResolver(bearerToken);
        }

        return null;
    }

    public Authentication gAuthentication(String token) {
        
        if (!StringUtils.hasText(token)) return null;

        var username = getUserName(token);
        var user     = userService.getByUsername(username);

        return buildAuthentication(user);
    }

    public String buildToken(String username, List<String> rules) {

        var claims = buildClaims(username);

        setClaimsDates(claims);
        setClaimsRules(claims, rules);

        //@formatter:off
        return Jwts
            .builder()
            .setClaims(claims)
            .signWith(SIGNATURE_ALGORITHM, secretKey)
            .compact();
        //@formatter:on

    }

    private Claims buildClaims(String username) {
        return Jwts.claims().setSubject(username);
    }

    private Authentication buildAuthentication(UserDetails user) {
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    private void setClaimsRules(Claims claims, List<String> rules) {
        claims.put(CLAIMS_RULES_PROP, rules);
    }

    private void setClaimsDates(Claims claims) {

        var createdAt = new Date();
        var expireAt  = new Date(createdAt.getTime() + expireTimeout);

        claims.setIssuedAt(createdAt);
        claims.setExpiration(expireAt);
    }

    private String getUserName(String token) {
        //@formatter:off
        return Jwts
            .parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
        //@formatter:on
    }

    private boolean isBearerToken(String bearerToken) {
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX);
    }
    
    private String bearerTokenResolver(String bearerToken) {
        return bearerToken.replace(BEARER_PREFIX, "").trim();
    }
}
