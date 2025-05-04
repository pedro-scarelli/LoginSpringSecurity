package com.login.user.services;

import com.login.user.domain.dtos.response.UserResponseDTO;
import com.login.user.domain.dtos.request.*;
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
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;


    public List<UserResponseDTO> getAllUsers(int page, int items) {
        var users = usersRepository.findAll(PageRequest.of(page-1, items));
        var usersResponseDto = StreamSupport.stream(users.spliterator(), false)
            .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getLogin()))
            .collect(Collectors.toList());

        return usersResponseDto;
    }

    public User getUserById(UUID id) {
        var optionalUser = usersRepository.findById(id);

        if(optionalUser.isPresent()){
            var userFound = optionalUser.get();

            return userFound;
        }

        throw new UserNotFoundException();
    }

    public User getUserByLogin(String login) {
        var userFound = usersRepository.findByLogin(login);
        if(userFound == null){
            throw new UserNotFoundException();
        }

        return userFound;
    }

    public User registerUser(RegisterUserRequestDTO registerUserRequestDto) {
        var newUser = new User();
        BeanUtils.copyProperties(registerUserRequestDto, newUser);

        isUserCredentialsDuplicated(newUser.getLogin(), newUser.getEmail());

        var hashedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);
        usersRepository.save(newUser);

        return newUser;
    }

    public void isUserCredentialsDuplicated(String login, String email) {
        if (usersRepository.existsByLoginOrMail(login, email)) {
            throw new DuplicateCredentialsException();
        }
    }

    public User updateUser(UUID id, UpdateUserRequestDTO updateUserRequestDto) {
        var userToUpdate = getUserById(id);

        BeanUtils.copyProperties(updateUserRequestDto, userToUpdate);
        var hashedPassword = encodePassword(userToUpdate.getPassword());
        userToUpdate.setPassword(hashedPassword);
        usersRepository.save(userToUpdate);

        return userToUpdate;
    }

    public User deleteUser(UUID id) {
        var userToDelete = getUserById(id);
        usersRepository.delete(userToDelete);

        return userToDelete;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = usersRepository.findByLogin(login);
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
        var passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.encode(password);
    }
}
