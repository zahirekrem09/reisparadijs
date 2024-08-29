package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.Reservation;
import com.reisparadijs.reisparadijs.persistence.dao.ReservationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * @Author Ertugrul Aydin
 * @Project reisparadijs
 * @Created 07 August Wednesday 2024 - 12:00
*/

// Deze klasse dient als tussenlaag tussen de service laag en de data access laag voor Reservation entiteiten.

@Repository
public class ReservationRepository {
    private final ReservationDAO reservationDAO;

    @Autowired
    public ReservationRepository(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    public Reservation save(Reservation reservation) {
        return reservationDAO.save(reservation);
    }

    public void update(Reservation reservation) {
        reservationDAO.update(reservation);
    }

    public void delete(int id) {
        reservationDAO.delete(id);
    }

    public Optional<Reservation> findById(int id) {
        return reservationDAO.findById(id);
    }

    public List<Reservation> findAll() {
        return reservationDAO.findAll();
    }
}
