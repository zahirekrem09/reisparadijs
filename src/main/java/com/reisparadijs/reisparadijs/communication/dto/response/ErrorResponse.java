package com.reisparadijs.reisparadijs.communication.dto.response;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 12 August Monday 2024 - 10:22
 */
public class ErrorResponse {

    private String message;



    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
