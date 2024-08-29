package com.reisparadijs.reisparadijs.business.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/********************
 * @Author Ertugrul Aydin
 * @Project reisparadijs
 * @Created 07 August Wednesday 2024 - 10:50
 *******************/

public class ReservationAccommodation implements Comparable<ReservationAccommodation> {

    private static final Logger logger = LoggerFactory.getLogger(ReservationAccommodation.class);

    private int id;
    private Reservation reservation;
    private Accommodation accommodation;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Double pricePerDay = 0.0;
    private Map<GuestType, Integer> guests = new HashMap<>();

    public ReservationAccommodation() {
        logger.info("Instance van ReservationAccommodation aangemaakt (geen args constructor).");
    }

    public ReservationAccommodation(int id) {
        this.id = id;
    }

    public ReservationAccommodation(Reservation reservation, Accommodation accommodation, LocalDate checkinDate, LocalDate checkoutDate, double pricePerDay, Double serviceFee, Double cleaningFee) {
        this.reservation = reservation;
        this.accommodation = accommodation;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.pricePerDay = pricePerDay;
        logger.info("Nieuwe ReservationAccommodation aangemaakt.");
    }

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

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static GuestType fromValue(String value) {
            for (GuestType type : GuestType.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid guest type: " + value);
        }
    }

    // Method to add guests
    public void addGuest(GuestType guestType, int numberOfGuests) {
        guests.put(guestType, numberOfGuests);
    }

    // Getters and Setters for guests
    public Map<GuestType, Integer> getGuests() {
        return guests;
    }

    // Getters en Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
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

    public void setGuests(Map<GuestType, Integer> guests) {
        this.guests = guests;
    }

    // Vergelijkt deze reservering met een andere op basis van de incheckdatum.
    @Override
    public int compareTo(ReservationAccommodation other) {
        return this.checkinDate.compareTo(other.checkinDate);
    }

    @Override
    public String toString() {
        return "ReservationAccommodation{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", accommodation=" + accommodation +
                ", checkinDate=" + checkinDate +
                ", checkoutDate=" + checkoutDate +
                ", pricePerDay=" + pricePerDay +
                '}';
    }
}
