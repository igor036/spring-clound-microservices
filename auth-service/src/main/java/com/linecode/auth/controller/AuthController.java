package com.linecode.auth.controller;

import com.linecode.auth.config.jwt.JwtTokenService;
import com.linecode.auth.dto.UserAuthenticationDto;
import com.linecode.auth.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenService jwtTokenService;


    @PostMapping
    public ResponseEntity<String> login(@RequestBody UserAuthenticationDto authentication) {
        var user  = userService.getByAuthentication(authentication);
        var token = jwtTokenService.buildToken(user.getUsername(), user.getRules());
        return ok(token);
    }

    @GetMapping("test")
    public String test() {
        return "allowed!";
    }
    
}
