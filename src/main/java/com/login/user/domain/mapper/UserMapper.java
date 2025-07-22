package com.login.user.domain.mapper;

import com.login.user.domain.dto.request.user.CreateUserRequestDTO;
import com.login.user.domain.dto.response.user.UserResponseDTO;
import com.login.user.domain.model.UserEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AddressMapper.class })
public interface UserMapper {

    @Mapping(target = "isEnabled", ignore = true)
    UserResponseDTO toDto (UserEntity user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "otpCode", ignore = true)
    @Mapping(target = "otpTimestamp", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "enabled", constant = "false")
    @Mapping(target = "role", expression = "java(com.login.user.domain.model.enums.UserRole.USER)")

    @Mapping(source = "person.name", target = "name")
    @Mapping(source = "person.cpf", target = "cpf")
    @Mapping(source = "person.email", target = "email")
    @Mapping(source = "person.phone", target = "phone")
    @Mapping(source = "person.address", target = "address")
    @Mapping(target = "personType", expression = "java(com.login.user.domain.model.enums.PersonType.USER)")
    UserEntity toEntity (CreateUserRequestDTO dto);
}
