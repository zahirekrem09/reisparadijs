package com.reisparadijs.reisparadijs.communication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 13 August Tuesday 2024 - 11:29
 */
public record CreateFavoriteRequest(
        @NotNull(message = "accommodationId is mandatory")
        @Positive
        int accommodationId
) {
}
