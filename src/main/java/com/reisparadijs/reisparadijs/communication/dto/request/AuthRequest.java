package com.reisparadijs.reisparadijs.communication.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 22:44
 */
public record AuthRequest(

        @NotNull(message = "Identifier is mandatory") @Size(min = 4, message = "Identifier should be 4 characters long minimum")
        String identifier,

        @NotNull(message = "Password is mandatory") @Size(min = 8, message = "Password should be 8 characters long minimum")
        String password,

        boolean rememberMe

) {
}
