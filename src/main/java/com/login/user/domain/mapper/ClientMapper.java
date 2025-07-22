package com.login.user.domain.mapper;

import com.login.user.domain.dto.request.client.CreateClientRequestDTO;
import com.login.user.domain.model.ClientEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AddressMapper.class })
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "company", ignore = true)
    
    @Mapping(source = "person.name", target = "name")
    @Mapping(source = "person.cpf", target = "cpf")
    @Mapping(source = "person.email", target = "email")
    @Mapping(source = "person.phone", target = "phone")
    @Mapping(source = "person.address", target = "address")
    @Mapping(target = "personType", expression = "java(com.login.user.domain.model.enums.PersonType.CLIENT)")
    ClientEntity toEntity (CreateClientRequestDTO dto);
}
