package com.login.user.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.login.user.domain.dto.request.company.CreateCompanyRequestDTO;
import com.login.user.domain.model.CompanyEntity;

@Mapper(componentModel = "spring", uses = { AddressMapper.class })
public interface CompanyMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "clients", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "owner", ignore = true)
    CompanyEntity toEntity (CreateCompanyRequestDTO createCompanyRequestDto);
}
