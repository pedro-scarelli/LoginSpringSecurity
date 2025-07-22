package com.login.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.user.domain.dto.request.company.CreateCompanyRequestDTO;
import com.login.user.domain.dto.response.SuccessResponseDTO;
import com.login.user.service.CompanyService;
import com.login.user.util.ValidationUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@RequestMapping(path = "/v1/company", produces = "application/json")
@RestController
public class CompanyController {
    
    private final CompanyService companyService;
    
    @PostMapping(consumes = "application/json")
    public ResponseEntity<@NonNull Object> createCompany(@Valid @RequestBody CreateCompanyRequestDTO createCompanyRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.validationErrors(result));
        }

        var newCompany = companyService.createCompany(createCompanyRequestDto);
        var successResponse = new SuccessResponseDTO("Empresa cadastrada com sucesso", Map.of("companyId", newCompany.getId()));

        return ResponseEntity.created(null).body(successResponse);
    }
}
