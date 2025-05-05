package com.login.user.services;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.login.user.domain.dtos.request.RegisterUserRequestDTO;
import com.login.user.domain.dtos.request.UpdateUserRequestDTO;
import com.login.user.domain.dtos.response.UserPaginationResponseDTO;
import com.login.user.domain.dtos.response.UserResponseDTO;
import com.login.user.domain.exceptions.DuplicateCredentialsException;
import com.login.user.domain.exceptions.UserNotFoundException;
import com.login.user.domain.models.User;
import com.login.user.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public UserPaginationResponseDTO getAllUsers(int page, int items) {
        var users = userRepository.findAll(PageRequest.of(page - 1, items));
        var usersResponseDto = StreamSupport.stream(users.spliterator(), false)
            .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.isActive()))
            .collect(Collectors.toList());

        return new UserPaginationResponseDTO(
            users.getTotalElements(),
            users.getTotalPages(),
            usersResponseDto
        );
    }

    public User getUserById(UUID id) {
        var optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            var userFound = optionalUser.get();

            return userFound;
        }

        throw new UserNotFoundException();
    }

    public void save(User userToSave) {
        userRepository.save(userToSave);
    }

    public User getUserByEmail(String email) {
        var userFound = userRepository.findByEmail(email);
        if(userFound == null){
            throw new UserNotFoundException();
        }

        return userFound;
    }

    public User registerUser(RegisterUserRequestDTO registerUserRequestDto) {
        var newUser = new User();
        BeanUtils.copyProperties(registerUserRequestDto, newUser);
        newUser.setActive(false);

        isUserCredentialsDuplicated(newUser.getEmail());

        var hashedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);
        userRepository.save(newUser);
        emailService.sendSignUpEmail(newUser.getEmail(), newUser.getId());

        return newUser;
    }

    public void isUserCredentialsDuplicated(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateCredentialsException();
        }
    }

    public User updateUser(UUID id, UpdateUserRequestDTO updateUserRequestDto) {
        var userToUpdate = getUserById(id);

        BeanUtils.copyProperties(updateUserRequestDto, userToUpdate);
        if (updateUserRequestDto.password() != null) {
            var hashedPassword = encodePassword(userToUpdate.getPassword());
            userToUpdate.setPassword(hashedPassword);
        }
        userRepository.save(userToUpdate);

        return userToUpdate;
    }

    public User deleteUser(UUID id) {
        var userToDelete = getUserById(id);
        userRepository.delete(userToDelete);

        return userToDelete;
    }

    public void activateUser(UUID id) {
        var userToActivate = getUserById(id);
        userToActivate.setActive(true);
        userRepository.save(userToActivate);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);
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
