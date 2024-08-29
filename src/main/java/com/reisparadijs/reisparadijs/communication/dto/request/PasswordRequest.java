package com.reisparadijs.reisparadijs.communication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 15:26
 */
public record PasswordRequest(
        @Email
        @NotBlank
        String email
) {
}
