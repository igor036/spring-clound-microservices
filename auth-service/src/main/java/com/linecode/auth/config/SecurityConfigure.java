package com.linecode.auth.config;

import java.security.SecureRandom;

import com.linecode.linecodeframework.config.LinecodeSecurityConfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfigure extends LinecodeSecurityConfigure {
    
    private static final String LOGIN_END_POINT = "/user/auth";


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    } 

    @Override
    protected void configureRequestPermissions(HttpSecurity http) throws Exception {
        //@formatter:off
        http.authorizeRequests()
            .antMatchers(LOGIN_END_POINT).permitAll()
            .anyRequest().authenticated();
        //@formatter:off
    }
}
