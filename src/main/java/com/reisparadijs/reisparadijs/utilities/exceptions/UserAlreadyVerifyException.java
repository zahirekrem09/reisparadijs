package com.reisparadijs.reisparadijs.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 12 August Monday 2024 - 13:15
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyVerifyException  extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L; // ? for  serialization

    public UserAlreadyVerifyException() {
        super("User already verified");
    }

    public UserAlreadyVerifyException(final String message) {
        super(message);
    }
}
