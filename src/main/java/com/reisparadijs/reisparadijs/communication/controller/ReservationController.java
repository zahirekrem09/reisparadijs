package com.reisparadijs.reisparadijs.communication.controller;

import com.reisparadijs.reisparadijs.business.service.ReservationService;
import com.reisparadijs.reisparadijs.communication.dto.request.ReservationAccommodationRequestDTO;
import com.reisparadijs.reisparadijs.communication.dto.request.ReservationRequestDTO;
import com.reisparadijs.reisparadijs.communication.dto.response.ReservationAccommodationResponseDTO;
import com.reisparadijs.reisparadijs.communication.dto.response.ReservationResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(@Valid @RequestBody ReservationRequestDTO reservationRequestDTO) {
        ReservationResponseDTO savedReservation = reservationService.save(reservationRequestDTO);
        return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable int id) {
        ReservationResponseDTO reservation = reservationService.findById(id);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<ReservationResponseDTO> reservations = reservationService.findAll();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> updateReservation(@PathVariable int id, @RequestBody ReservationRequestDTO reservationRequestDTO) {
        ReservationResponseDTO updatedReservation = reservationService.update(id, reservationRequestDTO);
        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable int id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{reservationId}/accommodations/{accommodationId}")
    public ResponseEntity<ReservationAccommodationResponseDTO> getAccommodationByReservationId(@PathVariable int reservationId, @PathVariable int accommodationId) {
        ReservationAccommodationResponseDTO accommodation = reservationService.findAccommodationById(reservationId, accommodationId);
        return new ResponseEntity<>(accommodation, HttpStatus.OK);
    }
    @PutMapping("/{reservationId}/accommodations/{accommodationId}")
    public ResponseEntity<ReservationAccommodationResponseDTO> updateAccommodationByReservationId(@PathVariable int reservationId, @PathVariable int accommodationId, @RequestBody ReservationAccommodationRequestDTO ReservationAccommodationRequestDTO) {
        ReservationAccommodationResponseDTO updatedAccommodation = reservationService.updateReservationAccommodation(reservationId, accommodationId, ReservationAccommodationRequestDTO);
        return new ResponseEntity<>(updatedAccommodation, HttpStatus.OK);
    }

}
