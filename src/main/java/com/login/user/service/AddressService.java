package com.login.user.service;

import org.springframework.stereotype.Service;

import com.login.user.domain.model.AddressEntity;
import com.login.user.repository.AddressRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AddressService {
    
    private final AddressRepository addressRepository;

    public void save(AddressEntity addressEntity) {
        addressRepository.save(addressEntity);
    }
}
