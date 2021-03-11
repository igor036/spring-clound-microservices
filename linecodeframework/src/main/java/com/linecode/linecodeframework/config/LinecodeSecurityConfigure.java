package com.linecode.linecodeframework.config;

import java.security.SecureRandom;

import com.linecode.linecodeframework.config.jwt.LinecodeJwtTokenFilter;
import com.linecode.linecodeframework.config.jwt.LinecodeJwtTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public abstract class LinecodeSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private LinecodeJwtTokenService jwtTokenService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    } 

    @Override
    protected void configure(HttpSecurity http) throws Exception { 
        disableCsrf(http);
        disableHttpBasic(http);
        addJwtTokenFilter(http);
        configureRequestPermissions(http);
        configureRequestSessionManagement(http);
    }

    protected void disableHttpBasic(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
    }

    protected void disableCsrf(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }

    protected void configureRequestSessionManagement(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    protected void configureRequestPermissions(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
    }

    protected void addJwtTokenFilter(HttpSecurity http) throws Exception {
        var jwtFilter = new LinecodeJwtTokenFilter(jwtTokenService);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
