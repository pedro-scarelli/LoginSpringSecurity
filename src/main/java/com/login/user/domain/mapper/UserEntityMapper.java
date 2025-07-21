package com.login.user.domain.mapper;

import com.login.user.domain.dto.response.UserResponseDTO;
import com.login.user.domain.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    @Mapping(target = "isEnabled", ignore = true)
    UserResponseDTO toDto (UserEntity user);
}
