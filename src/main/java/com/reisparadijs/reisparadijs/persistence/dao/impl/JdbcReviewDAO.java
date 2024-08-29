package com.reisparadijs.reisparadijs.persistence.dao.impl;

import com.reisparadijs.reisparadijs.business.domain.ReservationAccommodation;
import com.reisparadijs.reisparadijs.business.domain.Review;
import com.reisparadijs.reisparadijs.communication.dto.response.AccommodationReviewResponse;
import com.reisparadijs.reisparadijs.persistence.dao.ReviewDAO;
import com.reisparadijs.reisparadijs.persistence.dao.ServiceRatingDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReviewDAO implements ReviewDAO {

    private final JdbcTemplate jdbcTemplate;
    private final ServiceRatingDAO serviceRatingDAO;

    public JdbcReviewDAO(JdbcTemplate jdbcTemplate,  ServiceRatingDAO serviceRatingDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.serviceRatingDAO = serviceRatingDAO;
    }

    private static class ReviewMapper implements RowMapper<Review> {
        @Override
        public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
            int id = rs.getInt("id");
            ReservationAccommodation reservationAccommodationId = new ReservationAccommodation();
            reservationAccommodationId.setId(rs.getInt("reservation_accommodation_id"));
            int rating = rs.getInt("rating");
            String comment = rs.getString("comment");
            var review = new Review(reservationAccommodationId, rating, comment);
            review.setId(id);
            return review;
        }
    }

    @Override
    public Review save(Review review) {
        String sql = "INSERT INTO review (reservation_accommodation_id, rating, comment) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                review.getReservationAccommodation().getId(),
                review.getRating(),
                review.getComment());

        Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        review.setId(id);
        return review;
    }

    @Override
    public void update(Review review) {
        String sql = "UPDATE review SET reservation_accommodation_id = ?, rating = ?, comment = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                review.getReservationAccommodation().getId(),
                review.getRating(),
                review.getComment(),
                review.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM review WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Review> findById(int id) {
        String sql = "SELECT * FROM review WHERE id = ?";
        List<Review> results = jdbcTemplate.query(sql, new ReviewMapper(), id);
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }

    @Override
    public List<Review> findAll() {
        String sql = "SELECT * FROM review";
        return jdbcTemplate.query(sql, new ReviewMapper());
    }

    public AccommodationReviewResponse findReviewAndServiceRatingByReservationAccommodationId(int accommodationId) {
        AccommodationReviewResponse accommodationReviewResponse = new AccommodationReviewResponse();
        accommodationReviewResponse.setReviews(findReviewByAccommdationId(accommodationId));
        accommodationReviewResponse.setServiceRatings(serviceRatingDAO.findServiceRatingByAccommdationId(accommodationId));
        return accommodationReviewResponse;
    }

    public List<Review> findReviewByAccommdationId(int accommodationId) {
        String sql = """
                SELECT r.*
                FROM review r
                JOIN reservation_accommodation ra ON r.reservation_accommodation_id = ra.id
                WHERE ra.accommodation_id = ?;
                """;

        return jdbcTemplate.query(sql, new ReviewMapper(), accommodationId);
    }


}
