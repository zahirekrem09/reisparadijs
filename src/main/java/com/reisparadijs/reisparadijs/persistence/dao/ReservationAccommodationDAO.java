package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.ReservationAccommodation;

import java.util.List;
import java.util.Optional;

/********************
 * @Author Ertugrul Aydin
 * @Project reisparadijs
 * @Created 07 August Wednesday 2024 - 11:00
 *******************/

// Deze interface definieert de methoden voor het uitvoeren van CRUD-bewerkingen op de ReservationAccommodation entiteit.

public interface ReservationAccommodationDAO extends GenericCrudDao<ReservationAccommodation> {
    // Hier kunnen Reservation specifieke methoden worden gedefinieerd

    List<ReservationAccommodation> findByReservationId(int reservationId);

}
