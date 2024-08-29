package com.reisparadijs.reisparadijs.communication.dto.request;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 16:07
 */
public record UpdateProfileRequest(

        @NotEmpty(message = "First name is mandatory")
        @NotNull(message = "First name is mandatory")
        @Size(min = 3, message = "First name should be 3 characters long minimum")
        String firstName,

        @NotEmpty(message = "Last name is mandatory")
        @NotNull(message = "Last name is mandatory")
        @Size(min = 3, message = "Last name should be 3 characters long minimum")
        String lastName,

        String infix,
        AppUser.Gender gender
) {
}
