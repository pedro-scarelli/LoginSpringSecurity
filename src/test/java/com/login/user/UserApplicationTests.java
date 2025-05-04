package com.login.user;

import com.login.user.domain.dtos.request.*;
import com.login.user.domain.exceptions.DuplicateCredentialsException;
import com.login.user.domain.exceptions.UserNotFoundException;
import com.login.user.domain.models.User;
import com.login.user.repositories.UsersRepository;
import com.login.user.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserByLogin() {
        var userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setLogin("login");
        user.setPassword("password");
    
        when(usersRepository.findByLogin(user.getUsername())).thenReturn(user);
    
        var foundUser = userService.getUserByLogin(user.getUsername());   
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getUsername(), foundUser.getLogin());
    }

    @Test
    void getUserByLoginFailure(){
        var login = "teste";

        try{
            when(userService.getUserByLogin(login)).thenReturn(null);
            fail();
        } catch(UserNotFoundException exception){
            System.out.println(exception.getMessage());
        }
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(createUser("John Doe", "john@example.com","login", "password"));
        users.add(createUser("Jane Smith","jane@example.com","login2", "password"));

        try{
            when(usersRepository.findAll()).thenReturn(users);
        } catch(UserNotFoundException exception){
            System.out.println(exception.getMessage());
            fail();
        }
    }

    @Test
    void registerUser() {
        var registerUserDto = new RegisterUserRequestDTO("John Doe", "john@example.com","login","password");
        var user = new User();
        user.setId(UUID.randomUUID());
        user.setName(registerUserDto.name());
        user.setEmail(registerUserDto.email());
		user.setLogin(registerUserDto.login());
        user.setPassword(new BCryptPasswordEncoder().encode(registerUserDto.password()));


        when(usersRepository.findByMail(registerUserDto.email())).thenReturn(null);
        when(usersRepository.save(any(User.class))).thenReturn(user);

        var savedUser = userService.registerUser(registerUserDto);

        assertNotNull(savedUser);
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getUsername(), savedUser.getLogin());
    }

    @Test
    void registerUserFalse() {
        var registerUserDto = new RegisterUserRequestDTO("John Doe", "john@example.com","login", "password");
        var existingUser = new User();

        when(usersRepository.findByMail(registerUserDto.email())).thenReturn(existingUser);

        try{
            userService.registerUser(registerUserDto);
            fail();
        } catch(DuplicateCredentialsException exception){
            System.out.println(exception.getMessage());
        }
    }

    @Test
    void updateUser() {
        var userId = UUID.randomUUID();
        var updatedUserDto = new UpdateUserRequestDTO("Updated Name", "updatedpassword");
        var existingUser = createUser("John Doe", "john@example.com","login", "password");

        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(usersRepository.save(any(User.class))).thenReturn(existingUser);

        var updatedUser = userService.updateUser(userId, updatedUserDto);

        assertEquals(updatedUserDto.name(), updatedUser.getName());
    }

    @Test
    void updateUserFailure() {
        var userId = UUID.randomUUID();
    
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        UpdateUserRequestDTO updatedUserDto = null;
        try{
            userService.updateUser(userId, updatedUserDto);
            fail();
        }catch(UserNotFoundException exception){
            System.out.println(exception.getMessage());
        }
    }

    @Test
    void deleteUser() {
        var userId = UUID.randomUUID();
        var existingUser = createUser("John Doe", "john@example.com", "login", "password");
    
        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    
        var deletedUserDto = userService.deleteUser(userId);
    
        assertEquals(existingUser.getName(), deletedUserDto.getName());
        assertEquals(existingUser.getEmail(), deletedUserDto.getEmail());
        assertEquals(existingUser.getLogin(), deletedUserDto.getLogin());
        assertEquals(existingUser.getPassword(), deletedUserDto.getPassword());
    }

    @Test
    void deleteUserFailure() {
        var userId = UUID.randomUUID();

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        try{
            userService.deleteUser(userId);
            fail();
        } catch(UserNotFoundException exception){
            System.out.println(exception.getMessage());
        }
    }


    private User createUser(String name, String mail, String login, String password) {
        var user = new User();
        user.setId(UUID.randomUUID());
        user.setName(name);
        user.setEmail(mail);
        user.setLogin(login);
        user.setPassword(new BCryptPasswordEncoder().encode(password));

        return user;
    }
}
