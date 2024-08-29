package com.reisparadijs.reisparadijs.business.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Blob;

public class Image {

    // Logger voor het loggen van informatie en gebeurtenissen in deze klasse
    private final Logger logger = LoggerFactory.getLogger(Image.class);

    // Velden van de klasse die de eigenschappen van een afbeelding beschrijven
    private int id;
    private Blob image;
    private Accommodation accommodation;

    // Lege constructor die een nieuw object van Image aanmaakt zonder parameters
    public Image() {
        logger.info("Instance of Image created (no-arg constructor).");
    }

    // Constructor die een nieuw object van Image aanmaakt met specifieke waarden voor id, image, en accommodation
    public Image(int id, Blob image, Accommodation accommodation) {
        this.id = id;
        this.image = image;
        this.accommodation = accommodation;
        logger.info("New Image created.");
    }

    // Methode om een stringrepresentatie van het Image object te geven
    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", image=" + image +
                ", accommodation=" + accommodation +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(int  id) {
        this.id = id;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }
}
