package com.login.user.domain.mapper;

import com.login.user.domain.dto.response.UserResponseDTO;
import com.login.user.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toDto (User user);
}
