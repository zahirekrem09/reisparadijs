package com.reisparadijs.reisparadijs.business.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/********************
 * @Author Ertugrul Aydin
 * @Project reisparadijs
 * @Created 07 August Wednesday 2024 - 09:30
 *******************/

public class Reservation implements Comparable<Reservation> {

    private static final Logger logger = LoggerFactory.getLogger(Reservation.class);

    private int id;
    private AppUser guest;
    private double totalPrice;
    private BookingStatus bookingStatus;
    private List<ReservationAccommodation> reservationAccommodations = new ArrayList<>();
    private LocalDateTime createdAt;

    public enum BookingStatus {
        AWAITING("Awaiting"),
        APPROVAL("Approval"),
        APPROVED("Approved"),
        CANCELLED("Cancelled"),
        COMPLETED("Completed");

        private final String value;

        BookingStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public Reservation(AppUser guest, double totalPrice, BookingStatus bookingStatus, LocalDateTime createdAt) {
        this.guest = guest;
        this.totalPrice = totalPrice;
        this.bookingStatus = bookingStatus;
        this.createdAt = createdAt;
        logger.info("Nieuwe reservering aangemaakt.");
    }

    public Reservation() {
        this.createdAt = LocalDateTime.now();
        logger.info("Instance van Reservering aangemaakt (geen args constructor).");
    }

    // Getters en Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AppUser getGuest() {
        return guest;
    }

    public void setGuest(AppUser guest) {
        this.guest = guest;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public List<ReservationAccommodation> getReservationAccommodations() {
        return reservationAccommodations;
    }

    public void setReservationAccommodations(List<ReservationAccommodation> reservationAccommodations) {
        this.reservationAccommodations = reservationAccommodations;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Vergelijkt deze reservering met een andere op basis van de aanmaakdatum.
    @Override
    public int compareTo(Reservation other) {
        return this.createdAt.compareTo(other.createdAt);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", guest=" + guest +
                ", totalPrice=" + totalPrice +
                ", bookingStatus=" + bookingStatus.getValue() +
                ", createdAt=" + createdAt +
                '}';
    }
}
