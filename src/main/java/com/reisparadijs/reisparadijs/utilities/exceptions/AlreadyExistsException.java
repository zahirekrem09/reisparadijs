package com.reisparadijs.reisparadijs.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AlreadyExistsException() {
        super("Already exists!");
    }

    public AlreadyExistsException(final String message) {
        super(message);
    }
}
