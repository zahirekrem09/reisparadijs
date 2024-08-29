package com.reisparadijs.reisparadijs.communication.dto.response;

import com.reisparadijs.reisparadijs.business.domain.Review;
import com.reisparadijs.reisparadijs.business.domain.ServiceRating;

import java.util.List;

public class AccommodationReviewResponse {

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<ServiceRating> getServiceRatings() {
        return serviceRatings;
    }

    public void setServiceRatings(List<ServiceRating> serviceRatings) {
        this.serviceRatings = serviceRatings;
    }

    private List<Review> reviews;
    private List<ServiceRating> serviceRatings;


}
