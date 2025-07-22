package com.login.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.login.user.domain.exception.DuplicateCredentialsException;
import com.login.user.domain.exception.UserNotFoundException;
import com.login.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
        var createUserDto = Mocks.createUserRequestDTO();

        when(userRepository.existsByEmail(createUserDto.person().email())).thenReturn(true);

        assertThrows(DuplicateCredentialsException.class, () -> userService.createUser(createUserDto));
    }
 }
