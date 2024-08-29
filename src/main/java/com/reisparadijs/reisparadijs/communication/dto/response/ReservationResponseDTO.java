package com.reisparadijs.reisparadijs.communication.dto.response;

import com.reisparadijs.reisparadijs.business.domain.Reservation.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationResponseDTO {

    private int id;
    private Integer guestId;
    private double totalPrice;
    private BookingStatus bookingStatus;
    private List<ReservationAccommodationResponseDTO> accommodations;
    private LocalDateTime createdAt;

    // Getters ve Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
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

    public List<ReservationAccommodationResponseDTO> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<ReservationAccommodationResponseDTO> accommodations) {
        this.accommodations = accommodations;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
