package com.reisparadijs.reisparadijs.persistence.dao;


import com.reisparadijs.reisparadijs.business.domain.Reservation;
import com.reisparadijs.reisparadijs.business.domain.Review;
import com.reisparadijs.reisparadijs.communication.dto.response.AccommodationReviewResponse;

import java.util.List;
import java.util.Optional;

public interface ReviewDAO extends GenericCrudDao<Review> {

    Optional<Review> findById(int id);

    List<Review> findAll();

    Review save(Review review);

    void update(Review review);

    void delete(int id);

    AccommodationReviewResponse findReviewAndServiceRatingByReservationAccommodationId(int accommodationId);
}
