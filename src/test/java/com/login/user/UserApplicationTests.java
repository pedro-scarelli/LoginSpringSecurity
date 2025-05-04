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

import com.login.user.domain.dtos.request.RegisterUserRequestDTO;
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
    void getUserByNonExistingLoginShouldThrowUserNotFoundException(){
        var login = "teste";
        when(userRepository.findByLogin(login)).thenReturn(null);

        try{
            userService.getUserByLogin(login);
            fail();
        } catch(UserNotFoundException exception) {}
    }

    @Test
    void registerDuplicatedUserCredentialsShouldThrowDuplicatedCredentialsException() {
        var registerUserDto = new RegisterUserRequestDTO("John Doe", "john@example.com", "login", "password");
        when(userRepository.existsByLoginOrMail("login", "john@example.com")).thenReturn(true);

        try{
            userService.registerUser(registerUserDto);
            fail();
        } catch(DuplicateCredentialsException exception) {}
    }

//     private User createUser(String name, String mail, String login, String password) {
//         var user = new User();
//         user.setId(UUID.randomUUID());
//         user.setName(name);
//         user.setEmail(mail);
//         user.setLogin(login);
//         user.setPassword(new BCryptPasswordEncoder().encode(password));
//
//         return user;
//     }
 }
