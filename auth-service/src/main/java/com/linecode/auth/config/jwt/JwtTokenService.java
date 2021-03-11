package com.linecode.auth.config.jwt;

import java.util.Date;
import java.util.List;

import com.linecode.auth.service.UserService;
import com.linecode.linecodeframework.config.jwt.LinecodeJwtTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenService  extends LinecodeJwtTokenService {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${security.jwt.token.expire-timeout}")
    private long expireTimeout;

    @Autowired
    private UserService userService;

    @Override
    public Authentication gAuthentication(String token) {
        
        if (!StringUtils.hasText(token)) return null;

        var username = getUserName(token);
        var user     = userService.getByUsername(username);

        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
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

    private void setClaimsRules(Claims claims, List<String> rules) {
        claims.put(CLAIMS_RULES_PROP, rules);
    }

    private void setClaimsDates(Claims claims) {

        var createdAt = new Date();
        var expireAt  = new Date(createdAt.getTime() + expireTimeout);

        claims.setIssuedAt(createdAt);
        claims.setExpiration(expireAt);
    }
}
