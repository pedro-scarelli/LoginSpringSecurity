package com.login.user;

import com.login.user.dtos.UserDto;
import com.login.user.exceptions.DuplicateCredentialsException;
import com.login.user.exceptions.UserNotFoundException;
import com.login.user.models.User;
import com.login.user.models.UserRole;
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
        user.setRole(UserRole.ADMIN);
    
        when(usersRepository.findByLogin(user.getUsername())).thenReturn(user);
    
        UserDto foundUser = userService.getUserByLogin(user.getUsername());   
        assertEquals(user.getName(), foundUser.name());
        assertEquals(user.getMail(), foundUser.mail());
        assertEquals(user.getUsername(), foundUser.login());
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
        UserDto userDto = new UserDto("John Doe", "john@example.com","login","password");
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(userDto.name());
        user.setMail(userDto.mail());
		user.setLogin(userDto.login());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.password()));
        user.setRole(UserRole.ADMIN);

        when(usersRepository.findByMail(userDto.mail())).thenReturn(null);
        when(usersRepository.save(any(User.class))).thenReturn(user);

        UserDto savedUser = userService.registerUser(userDto);

        assertNotNull(savedUser);
        assertEquals(user.getName(), savedUser.name());
        assertEquals(user.getMail(), savedUser.mail());
        assertEquals(user.getUsername(), savedUser.login());
    }

    @Test
    void registerUserFalse() {
        UserDto userDto = new UserDto("John Doe", "john@example.com","login", "password");
        User existingUser = new User();

        when(usersRepository.findByMail(userDto.mail())).thenReturn(existingUser);

        try{
            userService.registerUser(userDto);
            fail();
        } catch(DuplicateCredentialsException exception){
            System.out.println(exception.getMessage());
        }
    }

    @Test
    void updateUser() {
        UUID userId = UUID.randomUUID();
        UserDto updatedUserDto = new UserDto("Updated Name", "updated@example.com", "logi", "updatedpassword");
        User existingUser = createUser("John Doe", "john@example.com","login", "password");

        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(usersRepository.save(any(User.class))).thenReturn(existingUser);

        UserDto updatedUser = userService.updateUser(userId, updatedUserDto);

        assertEquals(updatedUserDto.name(), updatedUser.name());
        assertEquals(updatedUserDto.mail(), updatedUser.mail());
    }

    @Test
    void updateUserFailure() {
        UUID userId = UUID.randomUUID();
    
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        UserDto updatedUserDto = null;
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
    
        UserDto deletedUserDto = userService.deleteUser(userId);
    
        assertEquals(existingUser.getName(), deletedUserDto.name());
        assertEquals(existingUser.getMail(), deletedUserDto.mail());
        assertEquals(existingUser.getLogin(), deletedUserDto.login());
        assertEquals(existingUser.getPassword(), deletedUserDto.password());
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
        user.setRole(UserRole.ADMIN);
        return user;
    }
}
