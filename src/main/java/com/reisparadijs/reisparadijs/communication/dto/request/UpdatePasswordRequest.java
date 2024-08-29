package com.reisparadijs.reisparadijs.communication.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 15:49
 */
public record UpdatePasswordRequest(
        @NotNull(message = "oldPassword is mandatory") @Size(min = 8, message = "Password should be 8 characters long minimum")
        String oldPassword,

        @NotNull(message = "newPassword is mandatory") @Size(min = 8, message = "Password should be 8 characters long minimum")
        String newPassword
) {
}
