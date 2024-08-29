package com.reisparadijs.reisparadijs.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 12 August Monday 2024 - 10:31
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L; // ? for  serialization

    public NotFoundException() {
        super("Not found!");
    }

    public NotFoundException(final String message) {
        super(message);
    }
}
