package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.ReservationAccommodation;
import com.reisparadijs.reisparadijs.persistence.dao.ReservationAccommodationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * @Author Ertugrul Aydin
 * @Project reisparadijs
 * @Created 07 August Wednesday 2024 - 12:00
*/

// Deze klasse dient als tussenlaag tussen de service laag en de data access laag voor ReservationAccommodation entiteiten.

@Repository
public class ReservationAccommodationRepository {
    private final ReservationAccommodationDAO reservationAccommodationDAO;

    @Autowired
    public ReservationAccommodationRepository(ReservationAccommodationDAO reservationAccommodationDAO) {
        this.reservationAccommodationDAO = reservationAccommodationDAO;
    }

    public ReservationAccommodation save(ReservationAccommodation reservationAccommodation) {
        return reservationAccommodationDAO.save(reservationAccommodation);
    }

    public void update(ReservationAccommodation reservationAccommodation) {
        reservationAccommodationDAO.update(reservationAccommodation);
    }

    public void delete(int id) {
        reservationAccommodationDAO.delete(id);
    }

    public Optional<ReservationAccommodation> findById(int id) {
        return reservationAccommodationDAO.findById(id);
    }

    public List<ReservationAccommodation> findAll() {
        return reservationAccommodationDAO.findAll();
    }

    public List<ReservationAccommodation> findByReservationId(int reservationId) {
        return reservationAccommodationDAO.findByReservationId(reservationId);
    }



}
