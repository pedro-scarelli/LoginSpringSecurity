package com.login.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.login.user.domain.dto.request.appointment.CreateAppointmentRequestDTO;
import com.login.user.domain.dto.response.SuccessResponseDTO;
import com.login.user.util.ValidationUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@RequestMapping(path = "/v1/appointment", produces = "application/json")
@RestController
public class AppointmentController {


    @PostMapping(consumes = "application/json")
    public ResponseEntity<@NonNull Object> createAppointment(@Valid @RequestBody CreateAppointmentRequestDTO createAppointmentRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.validationErrors(result));
        }

        return ResponseEntity.created(null).body(new SuccessResponseDTO("Agenda criada com sucesso"));
    }
}
