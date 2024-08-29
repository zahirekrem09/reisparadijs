package com.reisparadijs.reisparadijs.business.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 13 August Tuesday 2024 - 11:07
 */
public class FavoriteAccommodation {

    private Integer id;
    private String title;
    private double pricePerDay;
    private int numberOfGuests;
    private int numberOfBedrooms;
    private int numberOfBathrooms;
    private int numberOfBeds;
    private List<Image> images;

    public FavoriteAccommodation(int id, int numberOfBeds, int numberOfBathrooms, int numberOfBedrooms,
                                 int numberOfGuests, double pricePerDay, String title) {
        this.id = id;
        this.numberOfBeds = numberOfBeds;
        this.numberOfBathrooms = numberOfBathrooms;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfGuests = numberOfGuests;
        this.pricePerDay = pricePerDay;
        this.title = title;
        this.images =new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Image> getImages() {
        return images;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }
}
