package com.reisparadijs.reisparadijs.communication.dto.request;

import jakarta.validation.constraints.NotNull;

public class AmenityDTO {

    @NotNull
    private String name;
    private String description;

    // Getters en Setters




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
