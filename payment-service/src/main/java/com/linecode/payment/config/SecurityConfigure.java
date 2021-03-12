package com.linecode.payment.config;

import com.linecode.linecodeframework.config.LinecodeSecurityConfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"dev", "prod"})
public class SecurityConfigure  extends  LinecodeSecurityConfigure {
 
}
