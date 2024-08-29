package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Reservation;
import com.reisparadijs.reisparadijs.business.domain.ReservationAccommodation;
import com.reisparadijs.reisparadijs.communication.dto.request.ReservationAccommodationRequestDTO;
import com.reisparadijs.reisparadijs.communication.dto.request.ReservationRequestDTO;
import com.reisparadijs.reisparadijs.communication.dto.response.ReservationAccommodationResponseDTO;
import com.reisparadijs.reisparadijs.communication.dto.response.ReservationResponseDTO;
import com.reisparadijs.reisparadijs.persistence.repository.AccommodationRepository;
import com.reisparadijs.reisparadijs.persistence.repository.ReservationAccommodationRepository;
import com.reisparadijs.reisparadijs.persistence.repository.ReservationRepository;
import com.reisparadijs.reisparadijs.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;
    private final ReservationAccommodationRepository reservationAccommodationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository,
                              AccommodationRepository accommodationRepository,
                              ReservationAccommodationRepository reservationAccommodationRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.accommodationRepository = accommodationRepository;
        this.reservationAccommodationRepository = reservationAccommodationRepository;
    }

    public ReservationResponseDTO save(ReservationRequestDTO reservationRequestDTO) {
        Reservation reservation = convertToEntity(reservationRequestDTO);
        Reservation savedReservation = reservationRepository.save(reservation);

        double totalPrice = calculateTotalPrice(savedReservation.getReservationAccommodations());

        for (ReservationAccommodation acc : savedReservation.getReservationAccommodations()) {
            acc.setReservation(savedReservation);
            reservationAccommodationRepository.save(acc);
        }

        savedReservation.setTotalPrice(totalPrice);
        reservationRepository.update(savedReservation);

        return convertToDTO(savedReservation);
    }

    public ReservationResponseDTO update(int id, ReservationRequestDTO reservationRequestDTO) {
        reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Reservation with ID " + id + " not found."));

        Reservation reservation = convertToEntity(reservationRequestDTO);
        reservation.setId(id);

        double totalPrice = calculateTotalPrice(reservation.getReservationAccommodations());
        reservation.setTotalPrice(totalPrice);
        reservationRepository.update(reservation);


        for (ReservationAccommodation acc : reservation.getReservationAccommodations()) {
            acc.setReservation(reservation);
                reservationAccommodationRepository.update(acc);
        }

        Reservation existingReservation = reservationRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Reservation with ID " + id + " not found."));
        existingReservation.setTotalPrice(totalPrice);
        existingReservation.setReservationAccommodations(reservationAccommodationRepository.findByReservationId(id));

        return convertToDTO(existingReservation);
    }

    public void delete(int id) {
        reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Reservation with ID " + id + " not found."));
        reservationRepository.delete(id);
    }

    public ReservationResponseDTO findById(int id) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    List<ReservationAccommodation> accommodations = reservationAccommodationRepository.findByReservationId(reservation.getId());
                    reservation.setReservationAccommodations(accommodations);
                    return convertToDTO(reservation);
                }).orElseThrow(() -> new IllegalArgumentException("Reservation with ID " + id + " not found."));
    }

    public List<ReservationResponseDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> {
                    List<ReservationAccommodation> accommodations = reservationAccommodationRepository.findByReservationId(reservation.getId());
                    reservation.setReservationAccommodations(accommodations);
                    return convertToDTO(reservation);
                })
                .collect(Collectors.toList());
    }

    public ReservationAccommodationResponseDTO findAccommodationById(int reservationId, int accommodationId) {

        ReservationAccommodation resaccommodation = reservationAccommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new IllegalArgumentException("Accommodation with ID " + accommodationId + " not found."));


        return convertToReservationAccommodationDTO(resaccommodation);
    }

    public ReservationAccommodationResponseDTO updateReservationAccommodation(int reservationId, int accommodationId, ReservationAccommodationRequestDTO ReservationAccommodationRequestDTO) {

        ReservationAccommodation existingReservationAccommodation = reservationAccommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new IllegalArgumentException("Accommodation with ID " + accommodationId + " not found."));

        if (existingReservationAccommodation.getReservation().getId() != reservationId) {
            throw new IllegalArgumentException("Accommodation with ID " + accommodationId + " does not belong to reservation with ID " + reservationId + ".");
        }

        ReservationAccommodationRequestDTO.setReservaionId(reservationId);

        reservationAccommodationRepository.update(convertToReservationAccommodationEntity(ReservationAccommodationRequestDTO));

        ReservationAccommodation updatedReservationAccommodation = reservationAccommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new IllegalArgumentException("Accommodation with ID " + accommodationId + " not found."));

        return convertToReservationAccommodationDTO(updatedReservationAccommodation);
    }




    private ReservationResponseDTO convertToDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setGuestId(reservation.getGuest().getId());
        dto.setTotalPrice(reservation.getTotalPrice());
        dto.setBookingStatus(reservation.getBookingStatus());
        dto.setCreatedAt(reservation.getCreatedAt());

        List<ReservationAccommodationResponseDTO> accommodationDTOs = reservation.getReservationAccommodations().stream()
                .map(this::convertToReservationAccommodationDTO)
                .collect(Collectors.toList());
        dto.setAccommodations(accommodationDTOs);

        return dto;
    }

    private Reservation convertToEntity(ReservationRequestDTO dto) {
        Reservation reservation = new Reservation();
        if (dto.getId() != 0) {
            reservation.setId(dto.getId());
        }

        AppUser guest = userRepository.findById(dto.getGuestId()).orElseThrow(() -> new IllegalArgumentException("Guest with ID " + dto.getGuestId() + " not found."));

        reservation.setGuest(guest);
        reservation.setBookingStatus(dto.getBookingStatus());

        if (dto.getAccommodations() != null && !dto.getAccommodations().isEmpty()) {
            List<ReservationAccommodation> accommodations = dto.getAccommodations().stream()
                    .map(this::convertToReservationAccommodationEntity)
                    .collect(Collectors.toList());
            reservation.setReservationAccommodations(accommodations);
        }

        return reservation;
    }

    private ReservationAccommodationResponseDTO convertToReservationAccommodationDTO(ReservationAccommodation reservationaccommodation) {
        ReservationAccommodationResponseDTO dto = new ReservationAccommodationResponseDTO();
        dto.setId(reservationaccommodation.getId());
        dto.setAccommodationId(reservationaccommodation.getAccommodation().getId());
        dto.setCheckinDate(reservationaccommodation.getCheckinDate());
        dto.setCheckoutDate(reservationaccommodation.getCheckoutDate());
        dto.setPricePerDay(reservationaccommodation.getPricePerDay());

        Map<ReservationAccommodationResponseDTO.GuestType, Integer> guestsDTO = reservationaccommodation.getGuests().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> convertGuestTypeToDTO(entry.getKey()),
                        Map.Entry::getValue
                ));
        dto.setGuests(guestsDTO);

        return dto;
    }

    private ReservationAccommodation convertToReservationAccommodationEntity(ReservationAccommodationRequestDTO dto) {
        ReservationAccommodation reservationAccommodation = new ReservationAccommodation();
        if (dto.getId() != 0) {
            reservationAccommodation.setId(dto.getId());
        }

        if (dto.getReservaionId() != null){
            Reservation reservation = reservationRepository.findById(dto.getReservaionId()).orElseThrow(() -> new IllegalArgumentException("Reservtion with ID " + dto.getReservaionId() + " not found."));
            reservationAccommodation.setReservation(reservation);
        }


        Optional<Accommodation> accommodation = accommodationRepository.findById(dto.getAccommodationId());
        reservationAccommodation.setAccommodation(accommodation.orElse(null));
        reservationAccommodation.setCheckinDate(dto.getCheckinDate());
        reservationAccommodation.setCheckoutDate(dto.getCheckoutDate());
        reservationAccommodation.setPricePerDay(dto.getPricePerDay());


        Map<ReservationAccommodation.GuestType, Integer> guestsEntity = dto.getGuests().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> convertGuestTypeToEntity(entry.getKey()),
                        Map.Entry::getValue
                ));
        guestsEntity.forEach(reservationAccommodation::addGuest);

        return reservationAccommodation;
    }

    private ReservationAccommodation.GuestType convertGuestTypeToEntity(ReservationAccommodationRequestDTO.GuestType dtoGuestType) {
        return ReservationAccommodation.GuestType.valueOf(dtoGuestType.name());
    }

    private ReservationAccommodationResponseDTO.GuestType convertGuestTypeToDTO(ReservationAccommodation.GuestType entityGuestType) {
        return ReservationAccommodationResponseDTO.GuestType.valueOf(entityGuestType.name());
    }

    private double calculateTotalPrice(List<ReservationAccommodation> accommodations) {
        double totalPrice = 0.0;

        for (ReservationAccommodation acc : accommodations) {
            LocalDate checkinDate = acc.getCheckinDate();
            LocalDate checkoutDate = acc.getCheckoutDate();

            if (!checkoutDate.isAfter(checkinDate)) {
                throw new IllegalArgumentException("Checkout date cannot be before or equal to check-in date.");
            }

            long daysBetween = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
            double accommodationPrice = daysBetween * acc.getPricePerDay();
            totalPrice += accommodationPrice;
        }

        return totalPrice;
    }
}
