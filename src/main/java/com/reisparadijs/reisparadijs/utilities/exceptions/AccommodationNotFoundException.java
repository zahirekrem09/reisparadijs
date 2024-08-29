package com.reisparadijs.reisparadijs.utilities.exceptions;

import java.io.Serial;

/*********************************************
 * @Author Mirre Cicilia
 * @Studentennummer 500241293
 * @Project Accommodation.java
 * @Created 14 August Wednesday 2024 - 19:34
 * @Korte beschrijving
 **********************************************/
public class AccommodationNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public AccommodationNotFoundException(final String message) {
        super(message);
    }
}
