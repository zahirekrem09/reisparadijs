package com.reisparadijs.reisparadijs.communication.dto.request;

import java.util.Optional;

public class LocationRadiusRequestDTO {

    private String location;
    private Optional<Integer> radius = Optional.empty();

    public LocationRadiusRequestDTO(String location, int radius) {
        this.location = location;
        this.radius = Optional.of(radius);
    }

    public String getLocation() {
        return location;
    }

    public Optional<Integer> getRadius() {
        return radius;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRadius(int radius) {
        this.radius = Optional.of(radius);
    }
}
