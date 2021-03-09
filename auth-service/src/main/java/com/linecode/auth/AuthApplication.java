package com.linecode.auth;

import java.util.Arrays;
import java.util.Collections;

import com.linecode.auth.entity.AuthorityRule;
import com.linecode.auth.entity.User;
import com.linecode.auth.repository.AuthorityRuleRepository;
import com.linecode.auth.repository.UserRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		
		var applicationContext      = SpringApplication.run(AuthApplication.class, args);
		var authorityRuleRepository = applicationContext.getBean(AuthorityRuleRepository.class);
		var userRepository 			= applicationContext.getBean(UserRepository.class);
		var passwordEncoder 	    = applicationContext.getBean(BCryptPasswordEncoder.class);

		userRepository.deleteAll();
		authorityRuleRepository.deleteAll();
		
		var rule = AuthorityRule.builder().description("admin").build();
		var user = User.builder()
			.username("igor.lima")
			.password(passwordEncoder.encode("123"))
			.accountNonExpired(false)
			.accountNonLocked(false)
			.credentialsNonExpired(false)
			.authorities(Collections.emptyList())
			.enabled(true)
			.build();


		rule = authorityRuleRepository.save(rule);
		user = userRepository.save(user);

		user.setAuthorities(Arrays.asList(rule));
		user = userRepository.save(user);

		System.out.println(userRepository.save(user));
	}
}
