package com.reisparadijs.reisparadijs.business.domain;

public class Review {

    private int id;
    private ReservationAccommodation reservationAccommodation;
    private int rating;
    private String comment;

    public Review() {}

    public Review( ReservationAccommodation reservationAccommodation, int rating, String comment) {
        this.reservationAccommodation = reservationAccommodation;
        this.rating = rating;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ReservationAccommodation getReservationAccommodation() {
        return reservationAccommodation;
    }

    public void setReservationAccommodation(ReservationAccommodation reservationAccommodation) {
        this.reservationAccommodation = reservationAccommodation;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
