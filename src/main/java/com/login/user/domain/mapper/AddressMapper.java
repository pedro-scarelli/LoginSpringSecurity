package com.login.user.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.login.user.domain.dto.request.address.CreateAddressRequestDTO;
import com.login.user.domain.model.AddressEntity;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    AddressEntity toEntity (CreateAddressRequestDTO createAddressRequestDto);
}
