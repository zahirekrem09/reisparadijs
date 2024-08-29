package com.reisparadijs.reisparadijs.utilities.exceptions;

import java.io.Serial;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 12 August Monday 2024 - 19:45
 */
public class AccessDeniedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public AccessDeniedException() {
        super("Access denied! You don't have permission to access this resource.");
    }
    public AccessDeniedException(String message) {
        super(message);
    }

}
