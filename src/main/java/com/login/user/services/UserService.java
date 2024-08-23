package com.login.user.services;

import com.login.user.domain.dtos.RegisterUserDto;
import com.login.user.domain.dtos.UserDto;
import com.login.user.domain.exceptions.DuplicateCredentialsException;
import com.login.user.domain.exceptions.UserNotFoundException;
import com.login.user.domain.models.User;
import com.login.user.repositories.UsersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;


    public List<UserDto> getAllUsers(int page, int items) {
        Iterable<User> users = usersRepository.findAll(PageRequest.of(page-1, items));
        System.out.println(users);
        boolean isEmpty = true;
        for (@SuppressWarnings("unused") User user : users) {
            isEmpty = false;
            break;
        }

        if (isEmpty) {
            throw new UserNotFoundException("Não existe nenhum usuário cadastrado ou você está tentando acessar uma página inexistente.");
        } 
        
        List<UserDto> usersDto = StreamSupport.stream(users.spliterator(), false)
            .map(user -> new UserDto(user.getId(), user.getName(), user.getMail(), user.getLogin()))
            .collect(Collectors.toList());

        return usersDto;
    }

    public User getUserById(UUID id) {
        Optional<User> optionalUser = usersRepository.findById(id);
        if(optionalUser.isPresent()){
            User userFound = optionalUser.get();
            return userFound;
        }
        throw new UserNotFoundException();
    }

    public User getUserByLogin(String login) {
        User userFound = usersRepository.findByLogin(login);
        if(userFound == null){
            throw new UserNotFoundException();
        }
        return userFound;
    }

    public User registerUser(RegisterUserDto registerUserDto) {
        User newUser = new User();
        BeanUtils.copyProperties(registerUserDto, newUser);

        if(usersRepository.findByMail(newUser.getMail()) != null || usersRepository.findByLogin(newUser.getUsername()) != null){
            throw new DuplicateCredentialsException();
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);
        
        usersRepository.save(newUser);
        return newUser;
    }

    public User updateUser(UUID id, RegisterUserDto updateUserDto) {
        User userToUpdate = getUserById(id);
        boolean duplicatedEmailOrLogin = usersRepository.existsByLoginOrMail(updateUserDto.login(), updateUserDto.mail());

        if ((!updateUserDto.mail().equals(userToUpdate.getMail()) || !updateUserDto.login().equals(userToUpdate.getUsername())) 
            && duplicatedEmailOrLogin) {
            throw new DuplicateCredentialsException();
        }

        BeanUtils.copyProperties(updateUserDto, userToUpdate);
        String hashedPassword = encodePassword(userToUpdate.getPassword());
        userToUpdate.setPassword(hashedPassword);
        usersRepository.save(userToUpdate);

        return userToUpdate;
    }

    public User deleteUser(UUID id) {
        User userToUpdate = getUserById(id);
        usersRepository.delete(userToUpdate);

        return userToUpdate;
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