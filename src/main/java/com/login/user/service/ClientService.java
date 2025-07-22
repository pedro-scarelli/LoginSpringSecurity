package com.login.user.service;

import org.springframework.stereotype.Service;

import com.login.user.domain.dto.request.client.CreateClientRequestDTO;
import com.login.user.domain.mapper.ClientMapper;
import com.login.user.domain.model.ClientEntity;
import com.login.user.repository.ClientRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClientService {
    
    private final ClientRepository clientRepository;

    private final CompanyService companyService;

    private final ClientMapper clientMapper;

    private final AddressService addressService;

    public ClientEntity createClient(CreateClientRequestDTO createClientRequestDto) {
        var clientCompany = companyService.getCompanyById(createClientRequestDto.companyId());

        var newClient = clientMapper.toEntity(createClientRequestDto);
        addressService.save(newClient.getAddress());
        newClient.setCompany(clientCompany);

        return clientRepository.save(newClient);
    }
}
