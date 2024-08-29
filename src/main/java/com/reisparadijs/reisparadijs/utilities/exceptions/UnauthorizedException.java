package com.reisparadijs.reisparadijs.utilities.exceptions;

import java.io.Serial;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 12 August Monday 2024 - 19:53
 */
public class UnauthorizedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public UnauthorizedException() {
        super("Unauthorized! You don't have permission to access this resource.");
    }
    public UnauthorizedException(String message) {
        super(message);
    }
}
