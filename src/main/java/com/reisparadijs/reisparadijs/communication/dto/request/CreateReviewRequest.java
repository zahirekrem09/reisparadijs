package com.reisparadijs.reisparadijs.communication.dto.request;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.ReservationAccommodation;
import com.reisparadijs.reisparadijs.business.domain.Review;
import com.reisparadijs.reisparadijs.business.domain.ServiceRating;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.HashMap;
import java.util.List;

public class CreateReviewRequest {

    private int reservationAccommodationId;

    private AppUser user;

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public int getReservationAccommodationId() {
        return reservationAccommodationId;
    }

    public void setReservationAccommodationId(int reservationAccommodationId) {
        this.reservationAccommodationId = reservationAccommodationId;
    }

    @NotNull
    @Positive
    private int rating;
    private String content;

    private List<ServiceRating> serviceRatings;


    public List<ServiceRating> getServiceRatings() {
        return serviceRatings;
    }


    public void setServiceRatings(List<ServiceRating> serviceRatings) {
        this.serviceRatings = serviceRatings;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Review toReview(CreateReviewRequest reviewRequest) {
        System.out.println(reviewRequest.getReservationAccommodationId());
        ReservationAccommodation reservationAccommodation = new ReservationAccommodation();
        reservationAccommodation.setId(reviewRequest.getReservationAccommodationId());
        return new Review(reservationAccommodation, reviewRequest.getRating(), reviewRequest.getContent());
    }


}
