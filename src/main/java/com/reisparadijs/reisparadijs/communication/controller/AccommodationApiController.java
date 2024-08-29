package com.reisparadijs.reisparadijs.communication.controller;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.AccommodationType;
import com.reisparadijs.reisparadijs.business.service.AccommodationService;
import com.reisparadijs.reisparadijs.business.service.AccommodationTypeService;
import com.reisparadijs.reisparadijs.business.service.SearchService;
import com.reisparadijs.reisparadijs.communication.dto.request.AccommodationDto;
import com.reisparadijs.reisparadijs.communication.dto.request.SearchRequest;
import com.reisparadijs.reisparadijs.persistence.repository.AccommodationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/*********************************************
 * @Author Mirre Cicilia
 * @Studentennummer 500241293
 * @Project reisparadijs
 * @Created 09 August Friday 2024 - 05:14
 * @Korte beschrijving: De AccommodationApiController is verantwoordelijk voor het afhandelen van HTTP-verzoeken van
 * de gebruiker en het retourneren van de juiste HTTP-responsen.
 **********************************************/
@RestController
@RequestMapping("api/accommodations")
public class AccommodationApiController {

    private static final Logger logger = LoggerFactory.getLogger(AccommodationApiController.class);

    private final AccommodationRepository accommodationRepository;
    private final AccommodationService accommodationService;
    private  final SearchService searchService;

    private final AccommodationTypeService accommodationTypeService;

    @Autowired
    public AccommodationApiController(AccommodationRepository accommodationRepository, AccommodationService accommodationService, SearchService searchService, AccommodationTypeService accommodationTypeService) {
        this.accommodationRepository = accommodationRepository;
        this.accommodationService = accommodationService;
        this.searchService = searchService;
        this.accommodationTypeService = accommodationTypeService;
        logger.info("New AccommodationApiController");
    }

    @PostMapping
    public ResponseEntity<Accommodation> createAccommodation(@RequestBody AccommodationDto accommodationDto) {
        Accommodation accommodation = accommodationService.save(accommodationDto);
        return new ResponseEntity<>(accommodation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Accommodation findAccommodationById(@PathVariable int id){
        return accommodationService.findById(id);
    }
 // todo :   @GetMapping ("/{id}/images") @Pieter met @Mirre samenwerken
    @PutMapping("/{id}")
    public ResponseEntity<Accommodation> updateAccommodation(@PathVariable int id, @RequestBody Accommodation accommodation) {
        if (id != accommodation.getId()) {
            throw new IllegalArgumentException("Accommodation ID in URL and request body do not match");
        }
        return ResponseEntity.ok(accommodationService.update(accommodation));
    }


    @GetMapping
    public List<Accommodation> findAllAccommodations(){
        return accommodationService.findAll();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Accommodation>> search(
            @RequestParam String location, @RequestParam int numberOfGuests, @RequestParam String checkinDate, @RequestParam String checkoutDate
    ) throws ParseException {
        SearchRequest request = new SearchRequest(location, numberOfGuests, checkinDate, checkoutDate);
        return ResponseEntity.ok(searchService.search(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        accommodationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/accommodation-types")
    public List<AccommodationType> findAllAccommodationTypes() {
        return accommodationTypeService.findAll();
    }

}
