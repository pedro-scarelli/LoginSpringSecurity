package com.login.user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.login.user.domain.dto.request.CreateUserRequestDTO;
import com.login.user.domain.exception.DuplicateCredentialsException;
import com.login.user.domain.exception.UserNotFoundException;
import com.login.user.repository.UserRepository;
import com.login.user.service.UserService;

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

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(randomUUID));
    }

    @Test
    void getUserByNonExistingEmailShouldThrowUserNotFoundException(){
        var email = "test@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(email));
    }

    @Test
    void registerDuplicatedUserCredentialsShouldThrowDuplicatedCredentialsException() {
        var createUserDto = new CreateUserRequestDTO("John Doe", "john@example.com", "password");
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(DuplicateCredentialsException.class, () -> userService.createUser(createUserDto));
    }
 }
