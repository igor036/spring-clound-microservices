package com.linecode.auth.service;

import com.linecode.auth.dto.UserAuthenticationDto;
import com.linecode.auth.entity.User;
import com.linecode.auth.exception.RestException;
import com.linecode.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.linecode.auth.util.ValidatorUtil.assertNotNull;
import static com.linecode.auth.util.ValidatorUtil.assertConstraints;

@Service
public class UserService {

    public static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found!";
    public static final String USER_NAME_NULL_OR_EMPTY_ERROR_MESSAGE = "The username is required!";
    public static final String MISSING_AUTHENTICATION_DATA_ERROR_MESSAGE = "Please enter your authentication data!";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User getByUsername(String username) throws UsernameNotFoundException {
        
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException(USER_NAME_NULL_OR_EMPTY_ERROR_MESSAGE);
        }

        var user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_ERROR_MESSAGE);
        }

        return user.get();
    }

    public User getByAuthentication(UserAuthenticationDto authentication) {

        assertNotNull(authentication, MISSING_AUTHENTICATION_DATA_ERROR_MESSAGE);
        assertConstraints(authentication);

        var userSearch = userRepository.findByUsername(authentication.getUsername());

        if (userSearch.isEmpty()) {
            throw new RestException(HttpStatus.FORBIDDEN, USER_NOT_FOUND_ERROR_MESSAGE);
        }

        var user = userSearch.get();

        if (!passwordEncoder.matches(authentication.getPassword(), user.getPassword())) {
            throw new RestException(HttpStatus.FORBIDDEN, USER_NOT_FOUND_ERROR_MESSAGE);
        }

        return user;
    }
}
