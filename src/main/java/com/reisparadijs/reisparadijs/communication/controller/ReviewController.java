package com.reisparadijs.reisparadijs.communication.controller;

import com.reisparadijs.reisparadijs.business.service.ReviewService;
import com.reisparadijs.reisparadijs.communication.dto.request.CreateReviewRequest;
import com.reisparadijs.reisparadijs.communication.dto.response.AccommodationReviewResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/accommodations/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    @PostMapping()
    public ResponseEntity<String> createReview(@RequestBody @Valid CreateReviewRequest reviewRequest) {
        reviewService.create(reviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("CREATED");
    }

    // todo convert entity to dto and convert dto to entity
    @GetMapping("/{accommodationId}")
    public ResponseEntity<AccommodationReviewResponse> getReviewAndServiceRating(@PathVariable int accommodationId) {
        AccommodationReviewResponse reviewAndServiceRating = reviewService.getReviewAndServiceRatingByReservationAccommodationId(accommodationId);
        return ResponseEntity.ok(reviewAndServiceRating);
    }
}
