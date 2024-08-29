package com.reisparadijs.reisparadijs.business.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Amenity {

    // Logger voor het loggen van informatie en gebeurtenissen in deze klasse
    private final Logger logger = LoggerFactory.getLogger(Amenity.class);

    // Velden van de klasse die de eigenschappen van een voorziening beschrijven
    private int id;
    private String name;
    private String description;

    // Lege constructor die een nieuw object van Amenity aanmaakt zonder parameters
    public Amenity() {
        logger.info("Instance of Amenity created (no-arg constructor).");
    }

    // Constructor die een nieuw object van Amenity aanmaakt met specifieke waarden voor id, name, en description
    public Amenity(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        logger.info("New Amenity created.");
    }

    // Methode om een stringrepresentatie van het Amenity object te geven
    @Override
    public String toString() {
        return "Amenity:" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description=" + description;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
