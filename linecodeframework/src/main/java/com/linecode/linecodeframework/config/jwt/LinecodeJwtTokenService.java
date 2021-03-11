package com.linecode.linecodeframework.config.jwt;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Jwts;

@SuppressWarnings("unchecked")
public abstract class LinecodeJwtTokenService {
 
    protected static final String BEARER_PREFIX = "Bearer ";
    protected static final String CLAIMS_RULES_PROP = "rules";
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    
    @Value("${security.jwt.token.secret-key}")
    protected String secretKey;

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
        var rules    = getUserRules(token);

        return new UsernamePasswordAuthenticationToken(username, "", rules);
    }

    protected String getUserName(String token) {
        //@formatter:off
        return Jwts
            .parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
        //@formatter:on
    }

    protected List<GrantedAuthority> getUserRules(String token) {
        //@formatter:off
        var rules = (List<String>)Jwts
            .parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody()
            .getOrDefault(CLAIMS_RULES_PROP, Collections.emptyList());

        return rules
            .stream()
            .map(this::buildGrantedAuthorityRule)
            .collect(Collectors.toList());
        //@formatter:on
    }

    protected GrantedAuthority buildGrantedAuthorityRule(String rule) {
        return () -> rule;
    }

    private boolean isBearerToken(String bearerToken) {
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX);
    }
    
    private String bearerTokenResolver(String bearerToken) {
        return bearerToken.replace(BEARER_PREFIX, "").trim();
    }
}
