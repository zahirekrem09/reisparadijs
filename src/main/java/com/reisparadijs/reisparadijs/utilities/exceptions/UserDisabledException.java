package com.reisparadijs.reisparadijs.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 12 August Monday 2024 - 18:10
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserDisabledException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L; // ? for  serialization

    public UserDisabledException() {
        super("User is disabled!");
    }

    public UserDisabledException(String message) {
        super(message);
    }
}
