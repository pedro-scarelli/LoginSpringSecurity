package com.login.user.domain.dto.request.appointment;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAppointmentRequestDTO(

    @NotNull(message = "O ID do cliente é obrigatório")
    UUID clientId,

    @NotNull(message = "O ID da empresa é obrigatório")
    UUID companyId,

    @NotNull(message = "O ID do endereço é obrigatório")
    UUID addressId,

    @NotNull(message = "A data agendada é obrigatória")
    @FutureOrPresent(message = "A data agendada deve ser no presente ou no futuro")
    LocalDateTime scheduledDate,

    @Positive(message = "A duração estimada deve ser positiva")
    Double estimatedDurationHours,

    @NotBlank(message = "A descrição do serviço é obrigatória")
    @Size(max = 10000, message = "A descrição do serviço deve ter no máximo 10000 caracteres")
    String serviceDescription,

    @Positive(message = "O valor estimado deve ser positivo")
    BigDecimal estimatedAmount,

    @Size(max = 10000, message = "As observações devem ter no máximo 10000 caracteres")
    String notes

) {}
