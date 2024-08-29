package com.reisparadijs.reisparadijs.persistence.dao.impl;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Reservation;
import com.reisparadijs.reisparadijs.persistence.dao.ReservationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/********************
 * @Author Ertugrul Aydin
 * @Project reisparadijs
 * @Created 07 August Wednesday 2024 - 10:52
 *******************/

// Deze klasse implementeert de CRUD-operaties voor de Reservation entiteit met behulp van JDBC.

@Repository
public class JdbcReservationDAO implements ReservationDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getInt("id"));
        reservation.setTotalPrice(rs.getDouble("total_price"));
        reservation.setBookingStatus(Reservation.BookingStatus.valueOf(rs.getString("booking_status").toUpperCase()));
        reservation.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

        int guestId = rs.getInt("guest_id");

        AppUser guest = new AppUser();
        guest.setId(guestId);
        reservation.setGuest(guest);

        return reservation;
    };

    public JdbcReservationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now();
        String sql = "INSERT INTO reservation (total_price, booking_status, guest_id, created_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, reservation.getTotalPrice(), reservation.getBookingStatus().name(), reservation.getGuest().getId(), now);
        Integer generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        reservation.setId(generatedId);
        reservation.setCreatedAt(now); // createdAt alanını set edin
        return reservation;
    }

    @Override
    public void update(Reservation reservation) {
        String sql = "UPDATE reservation SET total_price = ?, booking_status = ? WHERE id = ?";
        jdbcTemplate.update(sql, reservation.getTotalPrice(), reservation.getBookingStatus().name(), reservation.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Reservation> findById(int id) {
        try {
            String sql = "SELECT * FROM reservation WHERE id = ?";
            Reservation reservation = jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservation";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }
}
