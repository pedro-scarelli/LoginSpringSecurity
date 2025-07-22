package com.login.user.service;

import com.login.user.domain.dto.CreatePersonDataDTO;
import com.login.user.domain.dto.request.address.CreateAddressRequestDTO;
import com.login.user.domain.dto.request.user.CreateUserRequestDTO;
import com.login.user.domain.model.AddressEntity;

public class Mocks {

    public static CreatePersonDataDTO createPersonDataDTO() {
        return new CreatePersonDataDTO(
                "John Doe",
                "john@example.com",
                "12345678901",
                "5547999999999",
                createAddressRequestDTO()
        );
    }

    public static CreateUserRequestDTO createUserRequestDTO() {
        return new CreateUserRequestDTO(
                createPersonDataDTO(),
                "Testee#1"
        );
    }

    public static CreateAddressRequestDTO createAddressRequestDTO() {
        return new CreateAddressRequestDTO(
                "Rua das Flores",
                123,
                "Apartamento 101",
                "Centro",
                "Blumenau",
                "SC",
                "89010-000",
                "Brasil"
        );
    }

    public static AddressEntity address() {
        return AddressEntity.builder()          
                .street("Rua das Flores")
                .number(123)
                .complement("Apartamento 101")
                .neighborhood("Centro")
                .city("Blumenau")
                .state("SC")
                .zipCode("89010-000")
                .country("Brasil")
                .build();  
    }
}
