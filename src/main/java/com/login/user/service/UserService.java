package com.login.user.service;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.login.user.domain.mapper.UserMapper;
import com.login.user.domain.dto.request.user.CreateUserRequestDTO;
import com.login.user.domain.dto.request.user.UpdateUserRequestDTO;
import com.login.user.domain.dto.response.user.UserPaginationResponseDTO;
import com.login.user.domain.exception.*;
import com.login.user.domain.model.UserEntity;
import com.login.user.repository.UserRepository;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final UserMapper userMapper;

    private final AddressService addressService;


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

    public UserEntity getUserById(UUID id) {
        var optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            return optionalUser.get();
        }

        throw new UserNotFoundException();
    }

    public void save(UserEntity userToSave) {
        userRepository.save(userToSave);
    }

    public UserEntity getUserByEmail(String email) {
        var userFound = userRepository.findByEmail(email);
        if (userFound == null) {
            throw new UserNotFoundException();
        }

        return userFound;
    }

    public UserEntity createUser(CreateUserRequestDTO createUserRequestDto) {
        isUserCredentialsDuplicated(createUserRequestDto.person().email(), createUserRequestDto.person().cpf());
        var newUser = userMapper.toEntity(createUserRequestDto);

        var hashedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);

        addressService.save(newUser.getAddress());
        userRepository.save(newUser);
        emailService.sendSignUpEmail(newUser.getEmail(), newUser.getId());

        return newUser;
    }

    public void isUserCredentialsDuplicated(String email, String cpf) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateCredentialsException();
        }

        userExistsByCpf(cpf);
    }

    public void userExistsByCpf(String cpf) {
        if(userRepository.existsByCpf(cpf)){
            throw new DuplicateCredentialsException("CPF já esta sendo utilizado");
        }
    }

    public UserEntity updateUser(UUID id, UpdateUserRequestDTO updateUserRequestDto) {
        var userToUpdate = getUserById(id);

        BeanUtils.copyProperties(updateUserRequestDto, userToUpdate);
        if (updateUserRequestDto.password() != null) {
            var hashedPassword = encodePassword(userToUpdate.getPassword());
            userToUpdate.setPassword(hashedPassword);
        }
        userRepository.save(userToUpdate);

        return userToUpdate;
    }

    public UserEntity deleteUser(UUID id) {
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
