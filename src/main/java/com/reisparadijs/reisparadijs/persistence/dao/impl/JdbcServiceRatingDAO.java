package com.reisparadijs.reisparadijs.persistence.dao.impl;

import com.reisparadijs.reisparadijs.business.domain.ReservationAccommodation;
import com.reisparadijs.reisparadijs.business.domain.ServiceRating;
import com.reisparadijs.reisparadijs.persistence.dao.ServiceRatingDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcServiceRatingDAO implements ServiceRatingDAO {

    private final JdbcTemplate jdbcTemplate;

    public JdbcServiceRatingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private static class ServiceRatingMapper implements RowMapper<ServiceRating> {
        @Override
        public ServiceRating mapRow(ResultSet rs, int rowNum) throws SQLException {
            Integer id = rs.getInt("id");
            Integer rating = rs.getInt("rating");
            ReservationAccommodation reservationAccommodationId = new ReservationAccommodation();
            reservationAccommodationId.setId(rs.getInt("reservation_accommodation_id"));
            String serviceReviewStr = rs.getString("service_review");
            ServiceRating.ServiceReview serviceReview = ServiceRating.ServiceReview.fromValue(serviceReviewStr);

            return new ServiceRating(id, rating, serviceReview, reservationAccommodationId);
        }
    }


    @Override
    public ServiceRating save(ServiceRating serviceRating) {
        String sql = "INSERT INTO service_rating (rating, service_review, reservation_accommodation_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                serviceRating.getRating(),
                serviceRating.getServiceReview().getValue(),
                serviceRating.getReservationAccommodation().getId());

        Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        serviceRating.setId(id);
        return serviceRating;
    }

    @Override
    public void update(ServiceRating serviceRating) {
        String sql = "UPDATE service_rating SET rating = ?, service_review = ?, reservation_accommodation_id = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                serviceRating.getRating(),
                serviceRating.getServiceReview().name(),  // Convert enum to String
                serviceRating.getReservationAccommodation().getId(),
                serviceRating.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM service_rating WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<ServiceRating> findById(int id) {
        String sql = "SELECT * FROM service_rating WHERE id = ?";
        List<ServiceRating> results = jdbcTemplate.query(sql, new ServiceRatingMapper(), id);
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }

    @Override
    public List<ServiceRating> findAll() {
        String sql = "SELECT * FROM service_rating";
        return jdbcTemplate.query(sql, new ServiceRatingMapper());
    }


    @Override
    public List<ServiceRating> findServiceRatingByAccommdationId(int accommodationId) {
        String sql = """
                SELECT sr.*
                FROM service_rating sr
                JOIN reservation_accommodation ra ON sr.reservation_accommodation_id = ra.id
                WHERE ra.accommodation_id = ?;
                """;

        return jdbcTemplate.query(sql, new JdbcServiceRatingDAO.ServiceRatingMapper(), accommodationId);
    }
}
