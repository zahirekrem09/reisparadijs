package com.reisparadijs.reisparadijs.communication.dto.request;

import com.reisparadijs.reisparadijs.business.domain.Reservation.BookingStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationRequestDTO {

    private int id;

    @NotNull(message = "Guest ID is required.")
    private Integer guestId;
    private BookingStatus bookingStatus;
    @Valid
    private List<ReservationAccommodationRequestDTO> accommodations;

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

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public List<ReservationAccommodationRequestDTO> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<ReservationAccommodationRequestDTO> accommodations) {
        this.accommodations = accommodations;
    }
}
