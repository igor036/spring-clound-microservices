package com.linecode.payment.config;

import com.linecode.linecodeframework.config.LinecodeSecurityWithoutAuthConfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"test"})
public class SecurityTestConfigure extends LinecodeSecurityWithoutAuthConfigure {
    
}
