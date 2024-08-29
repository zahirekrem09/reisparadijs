package com.reisparadijs.reisparadijs.communication.dto.response;

import java.util.Map;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 12 August Monday 2024 - 10:22
 */
public class DetailedErrorResponse extends ErrorResponse {

    public DetailedErrorResponse(String message) {
        super(message);
    }

    Map<String, String> items;

    public DetailedErrorResponse(String message, Map<String, String> items) {
        super(message);
        this.items = items;
    }

    public DetailedErrorResponse() {
    }

    public Map<String, String> getItems() {
        return items;
    }

    public void setItems(Map<String, String> items) {
        this.items = items;
    }
}
