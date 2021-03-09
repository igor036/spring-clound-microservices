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

@Service
public class UserService {

    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found!";
    private static final String USER_NAME_NULL_OR_EMPTY_ERROR_MESSAGE = "The username is required!";
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User getByUsername(String username) throws UsernameNotFoundException {
        
        var user = userRepository.findByUsername(username);
        
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException(USER_NAME_NULL_OR_EMPTY_ERROR_MESSAGE);
        }

        if (user.isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_ERROR_MESSAGE);
        }

        return user.get();
    }

    public User getByAuthentication(UserAuthenticationDto authentication) {

        var userSearch = userRepository.findByUsername(authentication.getUserName());

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
