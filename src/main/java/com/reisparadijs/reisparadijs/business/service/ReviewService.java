package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.Review;
import com.reisparadijs.reisparadijs.communication.dto.request.CreateReviewRequest;
import com.reisparadijs.reisparadijs.communication.dto.response.AccommodationReviewResponse;
import com.reisparadijs.reisparadijs.persistence.repository.AccommodationRepository;
import com.reisparadijs.reisparadijs.persistence.repository.ReviewRepository;
import com.reisparadijs.reisparadijs.persistence.repository.ServiceRatingRepository;
import com.reisparadijs.reisparadijs.utilities.exceptions.AccommodationNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ServiceRatingRepository serviceRatingRepository;

    private  final AccommodationRepository accommodationRepository;

    public ReviewService(ReviewRepository reviewRepository, ServiceRatingRepository serviceRatingRepository, AccommodationRepository accommodationRepository) {
        this.reviewRepository = reviewRepository;
        this.serviceRatingRepository = serviceRatingRepository;
        this.accommodationRepository = accommodationRepository;
    }

    public Review create(CreateReviewRequest reviewRequest) {
        // stap 1 - save algemeen rating
        System.out.println(reviewRequest);
        ;
        reviewRepository.save(CreateReviewRequest.toReview(reviewRequest));
        //stap 2 servicerating
        reviewRequest.getServiceRatings().forEach(serviceRatingRepository::save);
        return CreateReviewRequest.toReview(reviewRequest);
    }

    // todo convert entity to dto and convert dto to entity
    public AccommodationReviewResponse getReviewAndServiceRatingByReservationAccommodationId(int accommodationId) {
        accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new AccommodationNotFoundException("Accommodation with ID " + accommodationId + " not found."));
        return reviewRepository.findReviewAndServiceRatingByReservationAccommodationId(accommodationId);
    }
}
