package com.reisparadijs.reisparadijs.communication.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/*********************************************
 * @Author Mirre Cicilia
 * @Studentennummer 500241293
 * @Project Accommodation.java
 * @Created 14 August Wednesday 2024 - 11:08
 * @Korte beschrijving: De AccommodationDto wordt gebruikt om alleen relevante velden door te geven
 * en te valideren.
 **********************************************/
public class AccommodationDto {

    private Integer id;
    private String zipCode;
    private String houseNumber;

    @NotBlank(message = "Fill in a valid title")
    private String title;
    @NotBlank(message = "Fill in a valid description")
    private String description;
    @DecimalMin(value = "0.0", message = "Must be at least 0.0")
    private double pricePerDay;
    @Min(value = 1, message = "Must be at least 1")
    private Integer numberOfGuests;
    private Integer numberOfBedrooms;
    @Min(value = 0, message = "Must be at least 0")
    private Integer numberOfBathrooms;
    @Min(value = 0, message = "Must be at least 0")
    private Integer numberOfBeds;
    private LocalDateTime publishedAt;
    private Boolean isActive = true; // default waarde
    private Integer hostId;
    @NotNull(message = "Must be one of the following values: 'House', 'Apartment', 'GuestHouse' or 'Hotel'")
    private Integer accommodationTypeId;
    private Integer parkLocationId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(Integer numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public Integer getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(Integer numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public Integer getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(Integer numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    public Integer getAccommodationTypeId() {
        return accommodationTypeId;
    }

    public void setAccommodationTypeId(Integer accommodationTypeId) {
        this.accommodationTypeId = accommodationTypeId;
    }

    public Integer getParkLocationId() {
        return parkLocationId;
    }

    public void setParkLocationId(Integer parkLocationId) {
        this.parkLocationId = parkLocationId;
    }


    @Override
    public String toString() {
        return "AccommodationDto{" +
                "id=" + id +
                ", zipCode='" + zipCode + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pricePerDay=" + pricePerDay +
                ", numberOfGuests=" + numberOfGuests +
                ", numberOfBedrooms=" + numberOfBedrooms +
                ", numberOfBathrooms=" + numberOfBathrooms +
                ", numberOfBeds=" + numberOfBeds +
                ", publishedAt=" + publishedAt +
                ", isActive=" + isActive +
                ", hostId=" + hostId +
                ", accommodationTypeId=" + accommodationTypeId +
                ", parkLocationId=" + parkLocationId +
                '}';
    }
}
