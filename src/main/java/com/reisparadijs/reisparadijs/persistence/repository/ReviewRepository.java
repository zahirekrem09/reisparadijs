package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.*;
import com.reisparadijs.reisparadijs.communication.dto.response.AccommodationReviewResponse;
import com.reisparadijs.reisparadijs.persistence.dao.ReviewDAO;
import com.reisparadijs.reisparadijs.persistence.dao.ServiceRatingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepository {

    private final ReviewDAO reviewDAO;
    private final ServiceRatingDAO serviceRatingDAO;

    @Autowired
    public ReviewRepository(ReviewDAO reviewDAO, ServiceRatingDAO serviceRatingDAO) {
        this.reviewDAO = reviewDAO;
        this.serviceRatingDAO = serviceRatingDAO;
    }

    public Review save(Review review) {
        return reviewDAO.save(review);
    }
    public void update(Review review) {
        reviewDAO.update(review);
    }

    public void delete(int id) {
        reviewDAO.delete(id);
    }

    public Optional<Review> findById(int id) {
        return reviewDAO.findById(id);
    }

    public List<Review> findAll() {
        return reviewDAO.findAll();
    }

    public AccommodationReviewResponse findReviewAndServiceRatingByReservationAccommodationId(int accommodationId) {
        return reviewDAO.findReviewAndServiceRatingByReservationAccommodationId(accommodationId);
    }


}
