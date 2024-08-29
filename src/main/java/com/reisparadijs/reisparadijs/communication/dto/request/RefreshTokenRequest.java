package com.reisparadijs.reisparadijs.communication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 14:39
 */
public record RefreshTokenRequest(
        @NotNull
        @NotBlank
        String refreshToken
) {
}
