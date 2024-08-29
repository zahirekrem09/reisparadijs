package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.Reservation;
import com.reisparadijs.reisparadijs.persistence.dao.ReservationDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/********************
 * @Author Ertugrul Aydin
 * @Project reisparadijs
 * @Created 08 August Wednesday 2024 - 15:02
 *******************/

class ReservationRepositoryTest {

    @Mock
    private ReservationDAO reservationDAO;

    @InjectMocks
    private ReservationRepository reservationRepository;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservation = new Reservation();
        reservation.setTotalPrice(100.0);
        reservation.setBookingStatus(Reservation.BookingStatus.APPROVED);
    }

    @Test
    void save() {
        when(reservationDAO.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationRepository.save(reservation);
        assertNotNull(savedReservation);
        verify(reservationDAO, times(1)).save(any(Reservation.class));
    }

    @Test
    void update() {
        doNothing().when(reservationDAO).update(any(Reservation.class));

        reservationRepository.update(reservation);
        verify(reservationDAO, times(1)).update(any(Reservation.class));
    }

    @Test
    void delete() {
        doNothing().when(reservationDAO).delete(anyInt());

        reservationRepository.delete(1);
        verify(reservationDAO, times(1)).delete(anyInt());
    }

    @Test
    void findById() {
        when(reservationDAO.findById(anyInt())).thenReturn(Optional.of(reservation));

        Optional<Reservation> foundReservation = reservationRepository.findById(1);
        assertTrue(foundReservation.isPresent());
        verify(reservationDAO, times(1)).findById(anyInt());
    }

    @Test
    void findAll() {
        List<Reservation> reservations = List.of(reservation);
        when(reservationDAO.findAll()).thenReturn(reservations);

        List<Reservation> foundReservations = reservationRepository.findAll();
        assertFalse(foundReservations.isEmpty());
        assertEquals(1, foundReservations.size());
        verify(reservationDAO, times(1)).findAll();
    }
}
