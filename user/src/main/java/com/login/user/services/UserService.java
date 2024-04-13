package com.login.user.services;

import com.login.user.domain.dtos.UserDto;
import com.login.user.domain.exceptions.DuplicateCredentialsException;
import com.login.user.domain.exceptions.UserNotFoundException;
import com.login.user.domain.models.User;
import com.login.user.repositories.UsersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;


    public Iterable<User> getAllUsers() {
        Iterable<User> users = usersRepository.findAll();
        boolean isEmpty = true;
        for (@SuppressWarnings("unused") User user : users) {
            isEmpty = false;
            break;
        }

        if (isEmpty) {
            throw new UserNotFoundException("Não existe nenhum usuário cadastrado");
        } 
        return users;
    }

    public UserDto getUserByLogin(String login) {
        User user = usersRepository.findByLogin(login);
        if(user == null){
            throw new UserNotFoundException();
        }
        UserDto userDto = new UserDto(user.getName(), user.getMail(), user.getLogin(), user.getPassword());
        return userDto;
    }

    public UserDto registerUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        if(usersRepository.findByMail(user.getMail()) != null){
            throw new DuplicateCredentialsException("E-mail já está sendo utilizado por outro usuário");
        }else if(usersRepository.findByLogin(user.getUsername()) != null){
            throw new DuplicateCredentialsException("Login já está sendo utilizado por outro usuário");
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(hashedPassword);
        
        usersRepository.save(user);
        return userDto;
    }

    public UserDto updateUser(UUID id, UserDto userDto) {
        Optional<User> optionalUser = usersRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (!userDto.mail().equals(user.getMail())) {
                if (usersRepository.findByMail(userDto.mail()) != null) {
                    throw new DuplicateCredentialsException("O email fornecido já está em uso por outro usuário.");
                }
            }
    
            if (!userDto.login().equals(user.getUsername())) {
                if (usersRepository.findByLogin(userDto.login()) != null) {
                    throw new DuplicateCredentialsException("O login fornecido já está em uso por outro usuário.");
                }
            }

            BeanUtils.copyProperties(userDto, user);
            String hashedPassword = encodePassword(user.getPassword());
            user.setPassword(hashedPassword);
            
            usersRepository.save(user);
            return userDto;
        }
        throw new UserNotFoundException();
    }

    public UserDto deleteUser(UUID id) {
        Optional<User> optionalUser = usersRepository.findById(id);
        if (optionalUser.isPresent()) {
            usersRepository.delete(optionalUser.get());
            User user = optionalUser.get();
            UserDto userDto = new UserDto(user.getName(), user.getMail(), user.getLogin(), user.getPassword());
            BeanUtils.copyProperties(user, userDto);
            return userDto;
        }
        throw new UserNotFoundException();
    }

    public void deleteAllUsers() {
        usersRepository.deleteAll();
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = usersRepository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException();
        }
        
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles("USER")
            .build();
    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}