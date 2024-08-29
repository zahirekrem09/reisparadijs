package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Reservation;
import com.reisparadijs.reisparadijs.business.domain.ReservationAccommodation;
import com.reisparadijs.reisparadijs.persistence.dao.impl.JdbcReservationAccommodationDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReservationAccommodationDaoTest {

    @Autowired
    private JdbcReservationAccommodationDAO reservationAccommodationDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Reservation reservation;
    private Accommodation accommodation;

    @BeforeEach
    void setUp() {
        // Set up Accommodation and Reservation with mock data

        accommodation = new Accommodation();
        accommodation.setId(1);


        reservation = new Reservation();
        reservation.setId(1);
        AppUser guest = new AppUser(
                1, // ID
                "testuser", // userName
                "password123", // password
                "John", // firstName
                "van", // infix
                "Doe", // lastName
                "johndoe@example.com", // email
                AppUser.Gender.MALE, // gender
                null, // profileImage
                new Date(), // joinedAt
                true // enabled
        );
        reservation.setGuest(guest);
        reservation.setTotalPrice(200.0);
        reservation.setBookingStatus(Reservation.BookingStatus.APPROVED);


    }

    @Test
    void saveTest() throws ParseException {
        ReservationAccommodation reservationAccommodation = new ReservationAccommodation();
        reservationAccommodation.setReservation(reservation);
        reservationAccommodation.setAccommodation(accommodation);
        reservationAccommodation.setPricePerDay(100.00);
        reservationAccommodation.setCheckinDate(LocalDate.of(2024, 8, 15));
        reservationAccommodation.setCheckoutDate(LocalDate.of(2024, 8, 20));

        ReservationAccommodation savedReservationAccommodation = reservationAccommodationDAO.save(reservationAccommodation);
        assertNotNull(savedReservationAccommodation.getId());
    }

    @Test
    void updateTest() {
        ReservationAccommodation reservationAccommodation = new ReservationAccommodation();
        reservationAccommodation.setReservation(reservation);
        reservationAccommodation.setAccommodation(accommodation);
        reservationAccommodationDAO.save(reservationAccommodation);

        reservationAccommodation.setPricePerDay(300.0);
        reservationAccommodationDAO.update(reservationAccommodation);

        Optional<ReservationAccommodation> updatedReservationAccommodation = reservationAccommodationDAO.findById(reservationAccommodation.getId());
        assertTrue(updatedReservationAccommodation.isPresent());
        assertEquals(300.0, updatedReservationAccommodation.get().getPricePerDay());
    }

    @Test
    void deleteTest() {
        ReservationAccommodation reservationAccommodation = new ReservationAccommodation();
        reservationAccommodation.setReservation(reservation);
        reservationAccommodation.setAccommodation(accommodation);
        reservationAccommodationDAO.save(reservationAccommodation);

        reservationAccommodationDAO.delete(reservationAccommodation.getId());

        Optional<ReservationAccommodation> deletedReservationAccommodation = reservationAccommodationDAO.findById(reservationAccommodation.getId());
        assertFalse(deletedReservationAccommodation.isPresent());
    }

    @Test
    void findByIdTest() {
        ReservationAccommodation reservationAccommodation = new ReservationAccommodation();
        reservationAccommodation.setReservation(reservation);
        reservationAccommodation.setAccommodation(accommodation);
        reservationAccommodationDAO.save(reservationAccommodation);

        Optional<ReservationAccommodation> foundReservationAccommodation = reservationAccommodationDAO.findById(reservationAccommodation.getId());
        assertTrue(foundReservationAccommodation.isPresent());
    }

    @Test
    void findAllTest() {
        ReservationAccommodation reservationAccommodation1 = new ReservationAccommodation();
        reservationAccommodation1.setReservation(reservation);
        reservationAccommodation1.setAccommodation(accommodation);
        reservationAccommodationDAO.save(reservationAccommodation1);

        ReservationAccommodation reservationAccommodation2 = new ReservationAccommodation();
        reservationAccommodation2.setReservation(reservation);
        reservationAccommodation2.setAccommodation(accommodation);
        reservationAccommodationDAO.save(reservationAccommodation2);

        List<ReservationAccommodation> reservationAccommodationList = reservationAccommodationDAO.findAll();
        assertFalse(reservationAccommodationList.isEmpty());
        assertEquals(2, reservationAccommodationList.size());
    }
}
