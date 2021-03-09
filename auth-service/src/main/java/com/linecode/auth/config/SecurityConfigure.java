package com.linecode.auth.config;

import java.security.SecureRandom;

import com.linecode.auth.config.jwt.JwtTokenFilter;
import com.linecode.auth.config.jwt.JwtTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfigure extends WebSecurityConfigurerAdapter {
    
    private static final String LOGIN_END_POINT = "/user/auth";

    @Autowired
    private JwtTokenService jwtTokenService;

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

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

    private void disableHttpBasic(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
    }

    private void disableCsrf(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }

    private void configureRequestSessionManagement(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private void configureRequestPermissions(HttpSecurity http) throws Exception {
        //@formatter:off
        http.authorizeRequests()
            .antMatchers(LOGIN_END_POINT).permitAll()
            .anyRequest().authenticated();
        //@formatter:off
    }

    private void addJwtTokenFilter(HttpSecurity http) throws Exception {
        var jwtFilter = new JwtTokenFilter(jwtTokenService);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
