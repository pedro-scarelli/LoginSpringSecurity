package com.login.user;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.login.user.domain.dtos.request.CreateUserRequestDTO;
import com.login.user.domain.exceptions.DuplicateCredentialsException;
import com.login.user.domain.exceptions.UserNotFoundException;
import com.login.user.repositories.UserRepository;
import com.login.user.services.UserService;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserByNonExistingIdShouldThrowUserNotFoundException() {
        var randomUUID = UUID.randomUUID();
        when(userRepository.findById(randomUUID)).thenReturn(Optional.empty());

        try{
            userService.getUserById(randomUUID);
            fail();
        } catch(UserNotFoundException exception) {}
    }

    @Test
    void getUserByNonExistingEmailShouldThrowUserNotFoundException(){
        var email = "test@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        try{
            userService.getUserByEmail(email);
            fail();
        } catch(UserNotFoundException exception) {}
    }

    @Test
    void registerDuplicatedUserCredentialsShouldThrowDuplicatedCredentialsException() {
        var createUserDto = new CreateUserRequestDTO("John Doe", "john@example.com", "password");
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        try{
            userService.createUser(createUserDto);
            fail();
        } catch(DuplicateCredentialsException exception) {}
    }
 }
