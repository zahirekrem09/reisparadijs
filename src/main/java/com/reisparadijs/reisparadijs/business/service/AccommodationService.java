package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.AccommodationType;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.ParkLocation;
import com.reisparadijs.reisparadijs.communication.dto.request.AccommodationDto;
import com.reisparadijs.reisparadijs.persistence.repository.AccommodationRepository;
import com.reisparadijs.reisparadijs.persistence.repository.AccommodationTypeRepository;
import com.reisparadijs.reisparadijs.persistence.repository.ParkLocationRepository;
import com.reisparadijs.reisparadijs.persistence.repository.UserRepository;
import com.reisparadijs.reisparadijs.utilities.exceptions.AccommodationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/*********************************************
 * @Author Mirre Cicilia
 * @Studentennummer 500241293
 * @Project reisparadijs
 * @Created 09 August Friday 2024 - 05:18
 * @Korte beschrijving: De AccommodationService bevat de businesslogica van de applicatie. Hij verwerkt de aanvragen
 * die van de controller komen. De Service werkt met complete domeinobjecten (dit zijn objecten die alle informatie en
 * gedragingen bevatten voor een specifiek onderdeel van de applicatie.
 **********************************************/
@Service
public class AccommodationService {

    private static final Logger logger = LoggerFactory.getLogger(AccommodationService.class);

    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;
    private final AccommodationTypeRepository accommodationTypeRepository;
    private final ParkLocationRepository parkLocationRepository;

    public AccommodationService(AccommodationRepository accommodationRepository, UserRepository userRepository, AccommodationTypeRepository accommodationTypeRepository, ParkLocationRepository parkLocationRepository) {
        this.accommodationRepository = accommodationRepository;
        this.userRepository = userRepository;
        this.accommodationTypeRepository = accommodationTypeRepository;
        this.parkLocationRepository = parkLocationRepository;
        logger.info("New AccommodationService.");
    }

    public Accommodation save(AccommodationDto accommodationDto) {
        // zet dto om in object
        Optional <Accommodation> accoex = accommodationRepository.findByHouseNumerAndZipCode(accommodationDto.getHouseNumber(), accommodationDto.getZipCode());
        if (accoex.isPresent()) {
            throw new IllegalArgumentException("Accommodation zipcode and housenumber combination already exists");
        }
        Accommodation accommodation = accommodationDtoToEntity(accommodationDto);
        accommodationRepository.save(accommodation);
        return accommodation;
    }

    public Accommodation findById(int id) {
        logger.info("Find accommodation with ID: {}", id);
        return accommodationRepository.findById(id)
                .orElseThrow(() -> new AccommodationNotFoundException("Accommodation with ID " + id + " not found."));
    }

    public Accommodation update(Accommodation accommodation) {
        int accommodationId = accommodation.getId();
        Accommodation existingAccommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new AccommodationNotFoundException("Accommodation with ID " + accommodationId + " not found."));
        accommodationRepository.update(accommodation);
        return accommodation;
    }


    public List<Accommodation> findAll() {
        return accommodationRepository.findAll();
    }

    public void delete(int id){
        accommodationRepository.findById(id)
                .orElseThrow(() -> new AccommodationNotFoundException("Accommodation with ID " + id + " not found."));
        accommodationRepository.delete(id);
    }

    public List<Accommodation> findAccommodationsByNumberOfGuests(int numberOfGuests) {
        return accommodationRepository.findAccommodationsByNumberOfGuests(numberOfGuests);
    }
    public List<Integer> findAllAvailableAccommodationIds(Date availableFrom, Date availableUntill) {
        return accommodationRepository.findAllAvailableAccommodationIds(availableFrom, availableUntill);
    }

    // methode om dto om te zetten in een entiteit
    private Accommodation accommodationDtoToEntity(AccommodationDto accommodationDto) {
        Accommodation accommodation = new Accommodation();
        accommodation.setZipCode(accommodationDto.getZipCode());
        accommodation.setHouseNumber(accommodationDto.getHouseNumber());
        accommodation.setTitle(accommodationDto.getTitle());
        accommodation.setDescription(accommodationDto.getDescription());
        accommodation.setPricePerDay(accommodationDto.getPricePerDay());
        accommodation.setNumberOfGuests(accommodationDto.getNumberOfGuests());
        accommodation.setNumberOfBedrooms(accommodationDto.getNumberOfBedrooms());
        accommodation.setNumberOfBathrooms(accommodationDto.getNumberOfBathrooms());
        accommodation.setNumberOfBeds(accommodationDto.getNumberOfBeds());
        accommodation.setPublishedAt(accommodationDto.getPublishedAt());
        accommodation.setActive(accommodationDto.getActive());

        AppUser host = userRepository.findById(accommodationDto.getHostId()).get();
        accommodation.setHost(host);

        AccommodationType type = accommodationTypeRepository.findById(accommodationDto.getAccommodationTypeId()).get();
        accommodation.setAccommodationType(type);

        ParkLocation location = parkLocationRepository.getById(accommodationDto.getParkLocationId()).get();
        accommodation.setParkLocationId(location);
        // om een accommodatie te maken is er geen optie tot foto's opslaan nodig, daar is een aparte service klasse
        // voor nodig en endpoint dat is de taak voor de persoon die images doet

        return accommodation;
    }
}

