package com.login.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.login.user.domain.dto.request.company.CreateCompanyRequestDTO;
import com.login.user.domain.exception.CompanyNotFoundException;
import com.login.user.domain.mapper.CompanyMapper;
import com.login.user.domain.model.CompanyEntity;
import com.login.user.repository.CompanyRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CompanyService {
    
    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    private final UserService userService;

    private final AddressService addressService;
    
    public CompanyEntity createCompany(CreateCompanyRequestDTO createCompanyRequestDto) {
        var newCompany = companyMapper.toEntity(createCompanyRequestDto);
        
        var companyOwner = userService.getUserById(createCompanyRequestDto.ownerId());
        newCompany.setOwner(companyOwner);
        saveCompanyAddress(newCompany);

        return companyRepository.save(newCompany);
    }

    private void saveCompanyAddress(CompanyEntity newCompany) {
        if (newCompany.getAddress() == null) {
            newCompany.setAddress(newCompany.getOwner().getAddress());
            return;
        }

        addressService.save(newCompany.getAddress());
    }

    public CompanyEntity getCompanyById(UUID id) {
        var optionalCompany = companyRepository.findById(id);

        if(optionalCompany.isPresent()){
            return optionalCompany.get();
        }

        throw new CompanyNotFoundException();
    }
}
