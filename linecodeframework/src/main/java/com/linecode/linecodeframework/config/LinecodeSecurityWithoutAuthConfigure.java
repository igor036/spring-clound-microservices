package com.linecode.linecodeframework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public abstract class LinecodeSecurityWithoutAuthConfigure extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .httpBasic().disable()
            .csrf().disable()
            .authorizeRequests().antMatchers("/").permitAll();
    }
}
