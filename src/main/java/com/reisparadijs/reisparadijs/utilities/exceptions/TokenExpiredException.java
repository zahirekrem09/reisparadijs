package com.reisparadijs.reisparadijs.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 12 August Monday 2024 - 13:10
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Token is expired!");
    }

    public TokenExpiredException(final String message) {
        super(message);
    }
}
