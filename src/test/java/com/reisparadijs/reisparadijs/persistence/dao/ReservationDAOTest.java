package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Reservation;
import com.reisparadijs.reisparadijs.persistence.dao.impl.JdbcReservationDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationDAOTest {

    private JdbcReservationDAO reservationDAO;
    private AppUser guest;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationDAO = new JdbcReservationDAO(jdbcTemplate);
        guest = createTestUser();
    }

    private AppUser createTestUser() {
        return new AppUser(
                3, // ID
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
    }

    @Test
    @DisplayName("Reservation DAO Save Test with AppUser")
    void save_test_success_with_user() {
        // Step 2: Create a new Reservation object with the guest
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setTotalPrice(150.0);
        reservation.setBookingStatus(Reservation.BookingStatus.APPROVED);

        // Step 3: Save the Reservation
        Reservation savedReservation = reservationDAO.save(reservation);

        // Assertions to verify that the Reservation was saved correctly
        assertThat(savedReservation.getId()).isGreaterThan(0);
        assertThat(savedReservation.getGuest()).isEqualTo(guest);
    }

    @Test
    @DisplayName("Update Reservation Test")
    void update_test_success() {
        // Step 1: Create a new Reservation and save it
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setTotalPrice(150.0);
        reservation.setBookingStatus(Reservation.BookingStatus.APPROVED);
        Reservation savedReservation = reservationDAO.save(reservation);

        savedReservation.setTotalPrice(200.0);
        savedReservation.setBookingStatus(Reservation.BookingStatus.CANCELLED);
        reservationDAO.update(savedReservation);

        Optional<Reservation> updatedReservation = reservationDAO.findById(savedReservation.getId());
        assertThat(updatedReservation).isNotEmpty();
        assertThat(updatedReservation.get().getTotalPrice()).isEqualTo(200.0);
        assertThat(updatedReservation.get().getBookingStatus()).isEqualTo(Reservation.BookingStatus.CANCELLED);
    }

    @Test
    @DisplayName("Delete Reservation Test")
    void delete_test_success() {
        // Step 1: Create a new Reservation and save it
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setTotalPrice(150.0);
        reservation.setBookingStatus(Reservation.BookingStatus.APPROVED);
        Reservation savedReservation = reservationDAO.save(reservation);

        // Step 2: Delete the reservation
        reservationDAO.delete(savedReservation.getId());

        // Step 3: Try to retrieve the deleted reservation and verify it no longer exists
        Optional<Reservation> deletedReservation = reservationDAO.findById(savedReservation.getId());
        assertThat(deletedReservation).isEmpty();
    }

    @Test
    @DisplayName("Find Reservation by ID")
    void findById_test_success() {

        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setTotalPrice(150.0);
        reservation.setBookingStatus(Reservation.BookingStatus.APPROVED);

        Reservation savedReservation = reservationDAO.save(reservation);
        int generatedId = savedReservation.getId();

        Optional<Reservation> foundReservation = reservationDAO.findById(generatedId);

        assertThat(foundReservation).isNotEmpty();
        assertThat(foundReservation.get().getId()).isEqualTo(generatedId);
    }

    @Test
    @DisplayName("Find All Reservations Test")
    void findAll_test_success() {
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setTotalPrice(150.0);
        reservation.setBookingStatus(Reservation.BookingStatus.APPROVED);
        Reservation savedReservation = reservationDAO.save(reservation);

        // Assume there are reservations in the database
        List<Reservation> reservations = reservationDAO.findAll();

        // Assertions to verify that the list is not empty and contains the expected number of reservations
        assertThat(reservations).isNotEmpty();
        assertThat(reservations.size()).isGreaterThan(0);
    }
}
