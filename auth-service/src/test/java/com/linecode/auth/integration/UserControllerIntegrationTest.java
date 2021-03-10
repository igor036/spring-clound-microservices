package com.linecode.auth.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.linecode.auth.dto.UserAuthenticationDto;
import com.linecode.auth.service.UserService;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class UserControllerIntegrationTest extends IntegrationTest {
    
    private static final String AUTHENTICATION_URL = "/user/auth";

    @Test
    public void testAuthenticateWithExistingUser() {

        var authentication = UserAuthenticationDto.builder().username("admin").password("admin##!").build();
        var httpEntity     = new HttpEntity<>(authentication);
        var response       = restTemplate.exchange(AUTHENTICATION_URL, HttpMethod.POST, httpEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testAuthenticateWithNonExistingUser() {

        var authentication = UserAuthenticationDto.builder().username("aladin").password("admin##!").build();
        var httpEntity     = new HttpEntity<>(authentication);
        var response       = restTemplate.exchange(AUTHENTICATION_URL, HttpMethod.POST, httpEntity, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(UserService.USER_NOT_FOUND_ERROR_MESSAGE, response.getBody());
    }
}
