package com.linecode.auth.unit;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import com.linecode.auth.dto.UserAuthenticationDto;
import com.linecode.auth.entity.User;
import com.linecode.auth.exception.RestException;
import com.linecode.auth.exception.UnprocessableEntityException;
import com.linecode.auth.factory.UserAuthenticationDtoFactory;
import com.linecode.auth.repository.UserRepository;
import com.linecode.auth.service.UserService;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UserServiceUnitTest extends UnitTest {

    private final UserAuthenticationDtoFactory factory = new UserAuthenticationDtoFactory();

    @InjectMocks
    private UserService userService;
    
    @Mock(name = "userRepository")
    private UserRepository userRepository;

    @Mock(name = "passwordEncoder")
    private BCryptPasswordEncoder passwordEncoder;
    
    @BeforeMethod
    public void initMock() {
        MockitoAnnotations.openMocks(this);
        userRepository  = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = UserService.USER_NAME_NULL_OR_EMPTY_ERROR_MESSAGE)
    public void testGetByUsernameWithNullValue() {
        userService.getByUsername(null);
    }

    @Test
    public void testGetByUsernameNonExistingUser() {

        when(userRepository.findByUsername("admin"))
            .thenReturn(Optional.empty());

        try {
            userService.getByUsername("admin");
            fail(FAIL_MESSAGE);
        } catch(RestException ex) {
            assertRestExceptionMessage(ex, UserService.USER_NOT_FOUND_ERROR_MESSAGE);
            assertRestExceptionStatusCode(ex, HttpStatus.NOT_FOUND);
        }
    }

    @Test(dataProvider = "testGetByAuthenticationWithInvalidDataDataProvider")
    public void testGetByAuthenticationWithInvalidData(UserAuthenticationDto authentication, Collection<String> invalidFields) {
        try {
            userService.getByAuthentication(authentication);
            fail(FAIL_MESSAGE);
        } catch(UnprocessableEntityException ex) {
            assertUnprocessableEntityExceptionInvalidFields(ex, invalidFields);
        }
    }

    @Test
    public void testGetByAuthenticationNonExistingUser() {
        
        var authentication = factory.buildFakeInstance();
        var username = authentication.getUsername();

        when(userRepository.findByUsername(username))
            .thenReturn(Optional.empty());

        try {
            userService.getByAuthentication(authentication);
            fail(FAIL_MESSAGE);
        } catch(RestException ex) {
            assertRestExceptionMessage(ex, UserService.USER_NOT_FOUND_ERROR_MESSAGE);
            assertRestExceptionStatusCode(ex, HttpStatus.FORBIDDEN);
        }
    }

    @Test
    public void testGetByAuthenticationNotMatchesPassword() {
    
        var authentication = factory.buildFakeInstance();
        var username = authentication.getUsername();
        var user = mock(User.class);

        when(user.getPassword())
            .thenReturn(passwordEncoder.encode("123a"));
        when(userRepository.findByUsername(username))
            .thenReturn(Optional.of(user));

        try {
            userService.getByAuthentication(authentication);
            fail(FAIL_MESSAGE);
        } catch(RestException ex) {
            assertRestExceptionMessage(ex, UserService.USER_NOT_FOUND_ERROR_MESSAGE);
            assertRestExceptionStatusCode(ex, HttpStatus.FORBIDDEN);
        }
    }

    @DataProvider(name = "testGetByAuthenticationWithInvalidDataDataProvider")
    public Object[][] testGetByAuthenticationWithInvalidDataDataProvider() {

        var usernameNull  = factory.buildFakeInstance().builder().username(null).build();
        var passwordNull  = factory.buildFakeInstance().builder().password(null).build();
        var usernameEmpty = factory.buildFakeInstance().builder().username("").build();
        var passwordEmpty = factory.buildFakeInstance().builder().password("").build();

        var userAndPasswordNull = factory
            .buildFakeInstance()
            .builder()
            .username(null)
            .password(null)
            .build();

        var userAndPasswordEmpty = factory
            .buildFakeInstance()
            .builder()
            .username("")
            .password("")
            .build();

        //@formatter:off
        return new Object[][] {
            {usernameNull,  Arrays.asList("username")},
            {passwordNull,  Arrays.asList("password")},
            {usernameEmpty, Arrays.asList("username")},
            {passwordEmpty, Arrays.asList("password")},
            {passwordEmpty, Arrays.asList("password")},
            {userAndPasswordNull,  Arrays.asList("username", "password")},
            {userAndPasswordEmpty, Arrays.asList("username", "password")}
        };
        //@formatter:on
    }
}
