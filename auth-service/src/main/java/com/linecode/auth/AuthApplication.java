package com.linecode.auth;

import java.util.Arrays;
import java.util.Collections;

import com.linecode.auth.entity.AuthorityRule;
import com.linecode.auth.entity.User;
import com.linecode.auth.repository.AuthorityRuleRepository;
import com.linecode.auth.repository.UserRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
}
