package com.reisparadijs.reisparadijs.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 15 August Thursday 2024 - 18:51
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParentMessageNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public ParentMessageNotFoundException() {
        super("Parent message not found!");
    }
}
