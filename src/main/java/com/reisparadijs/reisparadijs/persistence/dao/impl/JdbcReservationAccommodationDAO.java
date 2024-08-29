package com.reisparadijs.reisparadijs.persistence.dao.impl;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.Reservation;
import com.reisparadijs.reisparadijs.business.domain.ReservationAccommodation;
import com.reisparadijs.reisparadijs.persistence.dao.ReservationAccommodationDAO;
import com.reisparadijs.reisparadijs.persistence.repository.ReservationRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/********************
 * @Author Ertugrul Aydin
 * @Project reisparadijs
 * @Created 07 August Wednesday 2024 - 10:15
 *******************/

// Deze klasse implementeert de CRUD-operaties voor de ReservationAccommodation entiteit met behulp van JDBC.

@Repository
public class JdbcReservationAccommodationDAO implements ReservationAccommodationDAO {

    private final JdbcTemplate jdbcTemplate;
    private final ReservationRepository reservationRepository;

    public JdbcReservationAccommodationDAO(JdbcTemplate jdbcTemplate, ReservationRepository reservationRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationRepository = reservationRepository;
    }

    private final RowMapper<ReservationAccommodation> reservationAccommodationRowMapper = (rs, rowNum) -> {
        ReservationAccommodation reservationAccommodation = new ReservationAccommodation();
        Reservation reservation = new Reservation();
        reservation.setId(rs.getInt("reservation_id"));
        reservationAccommodation.setReservation(reservation);
        reservationAccommodation.setId(rs.getInt("id"));
        reservationAccommodation.setCheckinDate(rs.getObject("checkin_date", LocalDate.class));
        reservationAccommodation.setCheckoutDate(rs.getObject("checkout_date", LocalDate.class));
        reservationAccommodation.setPricePerDay(rs.getDouble("price_per_day"));

        Accommodation accommodation = new Accommodation();
        accommodation.setId(rs.getInt("accommodation_id"));
        reservationAccommodation.setAccommodation(accommodation);
        return reservationAccommodation;
    };

    @Override
    public ReservationAccommodation save(ReservationAccommodation reservationAccommodation) {
        try {
            String sql = "INSERT INTO reservation_accommodation (reservation_id, accommodation_id, checkin_date, checkout_date, price_per_day) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, reservationAccommodation.getReservation().getId(), reservationAccommodation.getAccommodation().getId(), reservationAccommodation.getCheckinDate(), reservationAccommodation.getCheckoutDate(), reservationAccommodation.getPricePerDay());
            Integer generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            reservationAccommodation.setId(generatedId);

            saveGuests(reservationAccommodation);

            return reservationAccommodation;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error saving ReservationAccommodation", e);
        }
    }

    private void saveGuests(ReservationAccommodation reservationAccommodation) {
        try {
            String checkSql = "SELECT COUNT(*) FROM reservation_accommodation_guest WHERE reservation_accommodation_reservation_id = ? AND guest_type = ?";
            String insertSql = "INSERT INTO reservation_accommodation_guest (reservation_accommodation_reservation_id, guest_type, number_of_guests) VALUES (?, ?, ?)";

            for (Map.Entry<ReservationAccommodation.GuestType, Integer> entry : reservationAccommodation.getGuests().entrySet()) {
                int count = jdbcTemplate.queryForObject(checkSql, Integer.class, reservationAccommodation.getReservation().getId(), entry.getKey().name());

                if (count == 0) {
                    jdbcTemplate.update(insertSql, reservationAccommodation.getReservation().getId(), entry.getKey().name(), entry.getValue());
                }
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error saving guests for ReservationAccommodation", e);
        }
    }

    @Override
    public void update(ReservationAccommodation reservationAccommodation) {
        try {
            String sql = "UPDATE reservation_accommodation SET accommodation_id = ?, checkin_date = ?, checkout_date = ?, price_per_day = ? WHERE id = ?";
            jdbcTemplate.update(sql, reservationAccommodation.getAccommodation().getId(), reservationAccommodation.getCheckinDate(), reservationAccommodation.getCheckoutDate(), reservationAccommodation.getPricePerDay(), reservationAccommodation.getId());

            updateGuests(reservationAccommodation);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error updating ReservationAccommodation", e);
        }
    }

    private void updateGuests(ReservationAccommodation reservationAccommodation) {
        try {
            for (Map.Entry<ReservationAccommodation.GuestType, Integer> entry : reservationAccommodation.getGuests().entrySet()) {
                String updateSql = "UPDATE reservation_accommodation_guest SET number_of_guests = ? WHERE reservation_accommodation_reservation_id = ? AND guest_type = ?";
                jdbcTemplate.update(updateSql, entry.getValue(), reservationAccommodation.getReservation().getId(), entry.getKey().name());
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error updating guests for ReservationAccommodation", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM reservation_accommodation WHERE id = ?";
            jdbcTemplate.update(sql, id);
            deleteGuests(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error deleting ReservationAccommodation", e);
        }
    }

    private void deleteGuests(int reservationAccommodationId) {
        try {
            String deleteGuestsSql = "DELETE FROM reservation_accommodation_guest WHERE reservation_accommodation_reservation_id = ?";
            jdbcTemplate.update(deleteGuestsSql, reservationAccommodationId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error deleting guests for ReservationAccommodation", e);
        }
    }

    @Override
    public Optional<ReservationAccommodation> findById(int id) {
        try {
            String sql = "SELECT * FROM reservation_accommodation WHERE id = ?";
            ReservationAccommodation reservationAccommodation = jdbcTemplate.queryForObject(sql, reservationAccommodationRowMapper, id);

            assert reservationAccommodation != null;
            addGuestsToReservationAccommodation(reservationAccommodation);
                return Optional.of(reservationAccommodation);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public List<ReservationAccommodation> findAll() {
            String sql = "SELECT * FROM reservation_accommodation";
            List<ReservationAccommodation> reservationAccommodations = jdbcTemplate.query(sql, reservationAccommodationRowMapper);

            for (ReservationAccommodation reservationAccommodation : reservationAccommodations) {
                addGuestsToReservationAccommodation(reservationAccommodation);
            }
            return reservationAccommodations;
    }

    @Override
    public List<ReservationAccommodation> findByReservationId(int reservationId) {
        try {
            String sql = "SELECT * FROM reservation_accommodation WHERE reservation_id = ?";
            List<ReservationAccommodation> reservationAccommodations = jdbcTemplate.query(sql, reservationAccommodationRowMapper, reservationId);
            for (ReservationAccommodation reservationAccommodation : reservationAccommodations) {
                addGuestsToReservationAccommodation(reservationAccommodation);
            }
            return reservationAccommodations;
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList(); // Boş bir liste döndür
        }
    }

    private void addGuestsToReservationAccommodation(ReservationAccommodation reservationAccommodation) {
        String sql = "SELECT guest_type, number_of_guests FROM reservation_accommodation_guest WHERE reservation_accommodation_reservation_id = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, reservationAccommodation.getReservation().getId());


        for (Map<String, Object> row : rows) {
            String guestTypeValue = (String) row.get("guest_type");
            ReservationAccommodation.GuestType guestType = ReservationAccommodation.GuestType.valueOf(guestTypeValue.toUpperCase());

            int numberOfGuests = ((Number) row.get("number_of_guests")).intValue();
            reservationAccommodation.addGuest(guestType, numberOfGuests);
        }
    }
}
