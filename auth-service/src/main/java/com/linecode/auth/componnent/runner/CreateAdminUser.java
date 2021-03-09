package com.linecode.auth.componnent.runner;

import java.security.SecureRandom;
import java.util.Arrays;

import com.linecode.auth.entity.AuthorityRule;
import com.linecode.auth.entity.User;
import com.linecode.auth.repository.AuthorityRuleRepository;
import com.linecode.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateAdminUser implements ApplicationRunner {
    
    @Value("${admin.username}")
    private String username;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.rule}")
    private String rule;

    @Autowired
    private AuthorityRuleRepository authorityRuleRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createAdminUser();
    }

    private void createAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            //@formatter:off
            var passwordEncoder = getPasswordEncoder();
            var rules = Arrays.asList(createAdminRule());
            var user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .accountNonExpired(false)
                .accountNonLocked(false)
                .credentialsNonExpired(false)
                .authorities(rules)
                .enabled(true)
                .build();
            //@formatter:on
            userRepository.save(user);
        }
    }

    private AuthorityRule createAdminRule() {

        var ruleSearch = authorityRuleRepository.findByDescription(username);

        if (ruleSearch.isEmpty()) {
            var adminRule = AuthorityRule.builder().description(rule).build();
            return authorityRuleRepository.save(adminRule);
        }

        return ruleSearch.get();
    }

    private BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }
}
