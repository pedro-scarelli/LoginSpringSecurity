package com.login.user.service;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.login.user.domain.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.login.user.domain.dto.request.CreateUserRequestDTO;
import com.login.user.domain.dto.request.UpdateUserRequestDTO;
import com.login.user.domain.dto.response.UserPaginationResponseDTO;
import com.login.user.domain.exception.DuplicateCredentialsException;
import com.login.user.domain.exception.UserNotFoundException;
import com.login.user.domain.model.User;
import com.login.user.domain.model.enums.UserRole;
import com.login.user.repository.UserRepository;


@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final UserMapper userMapper;

    public UserPaginationResponseDTO getAllUsers(int page, int items) {
        var users = userRepository.findAll(PageRequest.of(page - 1, items));
        var usersResponseDto = StreamSupport.stream(users.spliterator(), false)
            .map(userMapper::toDto).collect(Collectors.toList());

        return new UserPaginationResponseDTO(
            users.getTotalElements(),
            users.getTotalPages(),
            usersResponseDto
        );
    }

    public User getUserById(UUID id) {
        var optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            return optionalUser.get();
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

    public User createUser(CreateUserRequestDTO createUserRequestDto) {
        var newUser = new User();
        BeanUtils.copyProperties(createUserRequestDto, newUser);
        newUser.setEnabled(false);

        isUserCredentialsDuplicated(newUser.getEmail());

        var hashedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);
        newUser.setRole(UserRole.USER);
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
        userToDelete.setDeletedAt(Instant.now());
        userRepository.save(userToDelete);

        return userToDelete;
    }

    public void activateUser(UUID id) {
        var userToActivate = getUserById(id);
        userToActivate.setEnabled(true);
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

    public String encodePassword(String password) {
        var passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.encode(password);
    }
}
