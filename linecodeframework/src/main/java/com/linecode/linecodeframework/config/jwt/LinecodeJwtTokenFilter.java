package com.linecode.linecodeframework.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class LinecodeJwtTokenFilter extends GenericFilterBean {

    private final LinecodeJwtTokenService jwtTokenService;

    public LinecodeJwtTokenFilter(LinecodeJwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        var token          = jwtTokenService.getToken((HttpServletRequest)request);
        var authentication = jwtTokenService.gAuthentication(token);

        setContextAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private void setContextAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
