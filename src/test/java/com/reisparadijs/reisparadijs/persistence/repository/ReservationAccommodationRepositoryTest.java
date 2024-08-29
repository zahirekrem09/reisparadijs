package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.ReservationAccommodation;
import com.reisparadijs.reisparadijs.persistence.dao.impl.JdbcReservationAccommodationDAO;
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

class ReservationAccommodationRepositoryTest {

    @Mock
    private JdbcReservationAccommodationDAO reservationAccommodationDAO;

    @InjectMocks
    private ReservationAccommodationRepository reservationAccommodationRepository;

    private ReservationAccommodation reservationAccommodation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationAccommodation = new ReservationAccommodation();
        reservationAccommodation.setId(1);
    }

    @Test
    void saveTest() {
        when(reservationAccommodationDAO.save(any(ReservationAccommodation.class))).thenReturn(reservationAccommodation);

        ReservationAccommodation savedReservationAccommodation = reservationAccommodationRepository.save(reservationAccommodation);
        assertNotNull(savedReservationAccommodation);
        verify(reservationAccommodationDAO, times(1)).save(any(ReservationAccommodation.class));
    }

    @Test
    void updateTest() {
        doNothing().when(reservationAccommodationDAO).update(any(ReservationAccommodation.class));

        reservationAccommodationRepository.update(reservationAccommodation);
        verify(reservationAccommodationDAO, times(1)).update(any(ReservationAccommodation.class));
    }

    @Test
    void deleteTest() {
        doNothing().when(reservationAccommodationDAO).delete(anyInt());

        reservationAccommodationRepository.delete(1);
        verify(reservationAccommodationDAO, times(1)).delete(anyInt());
    }

    @Test
    void findByIdTest() {
        when(reservationAccommodationDAO.findById(anyInt())).thenReturn(Optional.of(reservationAccommodation));

        Optional<ReservationAccommodation> foundReservationAccommodation = reservationAccommodationRepository.findById(1);
        assertTrue(foundReservationAccommodation.isPresent());
        verify(reservationAccommodationDAO, times(1)).findById(anyInt());
    }

    @Test
    void findAllTest() {
        List<ReservationAccommodation> reservationAccommodations = List.of(reservationAccommodation);
        when(reservationAccommodationDAO.findAll()).thenReturn(reservationAccommodations);

        List<ReservationAccommodation> foundReservationAccommodations = reservationAccommodationRepository.findAll();
        assertFalse(foundReservationAccommodations.isEmpty());
        assertEquals(1, foundReservationAccommodations.size());
        verify(reservationAccommodationDAO, times(1)).findAll();
    }
}
