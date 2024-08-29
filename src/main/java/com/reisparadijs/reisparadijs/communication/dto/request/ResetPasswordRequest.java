package com.reisparadijs.reisparadijs.communication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 15:31
 */
public record ResetPasswordRequest(
        @NotNull(message = "Password is mandatory") @Size(min = 8, message = "Password should be 8 characters long minimum")
        String password,

        @NotNull(message = "passwordConfirm is mandatory") @Size(min = 8, message = "Password should be 8 characters long minimum")
        String passwordConfirm
) {
}
