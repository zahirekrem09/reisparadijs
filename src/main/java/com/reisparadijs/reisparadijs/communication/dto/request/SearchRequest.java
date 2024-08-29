package com.reisparadijs.reisparadijs.communication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Date;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 14 August Wednesday 2024 - 11:35
 */
public record SearchRequest(

        @NotBlank
        @NotNull
        @Size(min = 3, message = "Location should be 3 characters long minimum")
        String location,

        @Positive
        @NotNull
        int numberOfGuests,

        @NotNull
        String checkinDate,
        // todo : check checkinDate moet in de toekomst liggen
        @NotNull
        String checkoutDate
        // todo : checkoutDate moet na checkinDate liggen(mag niet gelijk zijn aan checkinDate)

) {
}
