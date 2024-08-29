package com.reisparadijs.reisparadijs.business.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 8 Augustus 2024 - 11:00
 */

public class ParkLocation implements Comparable<ParkLocation> {
    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(ParkLocation.class);
    private int parkID;
    private String zipcode;
    private String number;
    private String description;
    private String name;
    private List<Amenity> parkAmenities;


    public ParkLocation(int parkID, String zipcode, String number, String description, String name, List<Amenity> parkAmenities) {
        this.parkID = parkID;
        this.zipcode = zipcode;
        this.number = number;
        this.description = description;
        this.name = name;
        this.parkAmenities = new ArrayList<>(parkAmenities);
        logger.info("ParkLocation created: (parkID is " + parkID + ")");
    }

    public ParkLocation(int parkID, String zipcode, String number, String description, String name) {
        this.parkID = parkID;
        this.zipcode = zipcode;
        this.number = number;
        this.description = description;
        this.name = name;
         this.parkAmenities = null;
        logger.info("ParkLocation created: (parkID is " + parkID + ")");
    }

    public ParkLocation(){};

    public ParkLocation(String zipcode, String number, String description, String name) {
        this.zipcode = zipcode;
        this.number = number;
        this.description = description;
        this.name = name;
    }

    public void setParkID(int parkID) {
        this.parkID = parkID;
    }

    public void setParkAmenities(List<Amenity> parkAmenities) {
        this.parkAmenities = parkAmenities;
    }

    public List<Amenity> getParkAmenities() {
        return parkAmenities;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getNumber() {
        return number;
    }

    public String getZipcode() {
        return zipcode;
    }

    public int getParkID() {
        return parkID;
    }

    public Logger getLogger() {
        return logger;
    }

    public void addAmenity(Amenity amenity) {
        if (this.parkAmenities == null) {
            this.parkAmenities = new ArrayList<>();
        }
        this.parkAmenities.add(amenity);
        logger.info("Amenity added to ParkLocation (parkID is " + this.parkID + "): " + amenity);
    }

    @Override
    public int compareTo(ParkLocation other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return this.parkID + " " + this.name;
    }

}
