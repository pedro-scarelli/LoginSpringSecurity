package com.login.user;

import com.login.user.domain.dtos.RegisterUserDto;
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
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setMail("john@example.com");
        user.setLogin("login");
        user.setPassword("password");
    
        when(usersRepository.findByLogin(user.getUsername())).thenReturn(user);
    
        User foundUser = userService.getUserByLogin(user.getUsername());   
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getMail(), foundUser.getMail());
        assertEquals(user.getUsername(), foundUser.getLogin());
    }

    @Test
    void getUserByLoginFailure(){
        String login = "teste";

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
        RegisterUserDto registerUserDto = new RegisterUserDto("John Doe", "john@example.com","login","password");
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(registerUserDto.name());
        user.setMail(registerUserDto.mail());
		user.setLogin(registerUserDto.login());
        user.setPassword(new BCryptPasswordEncoder().encode(registerUserDto.password()));


        when(usersRepository.findByMail(registerUserDto.mail())).thenReturn(null);
        when(usersRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.registerUser(registerUserDto);

        assertNotNull(savedUser);
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getMail(), savedUser.getMail());
        assertEquals(user.getUsername(), savedUser.getLogin());
    }

    @Test
    void registerUserFalse() {
        RegisterUserDto registerUserDto = new RegisterUserDto("John Doe", "john@example.com","login", "password");
        User existingUser = new User();

        when(usersRepository.findByMail(registerUserDto.mail())).thenReturn(existingUser);

        try{
            userService.registerUser(registerUserDto);
            fail();
        } catch(DuplicateCredentialsException exception){
            System.out.println(exception.getMessage());
        }
    }

    @Test
    void updateUser() {
        UUID userId = UUID.randomUUID();
        RegisterUserDto updatedUserDto = new RegisterUserDto("Updated Name", "updated@example.com", "logi", "updatedpassword");
        User existingUser = createUser("John Doe", "john@example.com","login", "password");

        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(usersRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.updateUser(userId, updatedUserDto);

        assertEquals(updatedUserDto.name(), updatedUser.getName());
        assertEquals(updatedUserDto.mail(), updatedUser.getMail());
    }

    @Test
    void updateUserFailure() {
        UUID userId = UUID.randomUUID();
    
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        RegisterUserDto updatedUserDto = null;
        try{
            userService.updateUser(userId, updatedUserDto);
            fail();
        }catch(UserNotFoundException exception){
            System.out.println(exception.getMessage());
        }
    }

    @Test
    void deleteUser() {
        UUID userId = UUID.randomUUID();
        User existingUser = createUser("John Doe", "john@example.com", "login", "password");
    
        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    
        User deletedUserDto = userService.deleteUser(userId);
    
        assertEquals(existingUser.getName(), deletedUserDto.getName());
        assertEquals(existingUser.getMail(), deletedUserDto.getMail());
        assertEquals(existingUser.getLogin(), deletedUserDto.getLogin());
        assertEquals(existingUser.getPassword(), deletedUserDto.getPassword());
    }

    @Test
    void deleteUserFailure() {
        UUID userId = UUID.randomUUID();

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        try{
            userService.deleteUser(userId);
            fail();
        } catch(UserNotFoundException exception){
            System.out.println(exception.getMessage());
        }
    }


    private User createUser(String name, String mail, String login, String password) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(name);
        user.setMail(mail);
        user.setLogin(login);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        return user;
    }
}
