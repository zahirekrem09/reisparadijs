package com.reisparadijs.reisparadijs.communication.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDate;
import java.util.Map;

public class ReservationAccommodationResponseDTO {
    private int id;
    private Integer accommodationId;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private double pricePerDay;
    private Map<GuestType, Integer> guests;

    // Enum for Guest Types
    public enum GuestType {
        ADULTS("Adults"),
        CHILDREN("Children"),
        INFANTS("Infants"),
        PETS("Pets");

        private final String value;

        GuestType(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static GuestType forValue(String value) {
            for (GuestType guestType : values()) {
                if (guestType.getValue().equalsIgnoreCase(value)) {
                    return guestType;
                }
            }
            throw new IllegalArgumentException("Invalid guest type: " + value);
        }
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(int accommodationId) {
        this.accommodationId = accommodationId;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Map<GuestType, Integer> getGuests() {
        return guests;
    }

    public void setGuests(Map<GuestType, Integer> guests) {
        this.guests = guests;
    }
}
