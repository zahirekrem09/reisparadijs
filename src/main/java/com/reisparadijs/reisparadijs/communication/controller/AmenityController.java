package com.reisparadijs.reisparadijs.communication.controller;

import com.reisparadijs.reisparadijs.business.domain.Amenity;
import com.reisparadijs.reisparadijs.business.service.AmenityService;
import com.reisparadijs.reisparadijs.communication.dto.request.AmenityDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;

    @Autowired
    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public ResponseEntity<List<Amenity>> getAllAmenities() {
        List<Amenity> amenities = amenityService.getAllAmenities();
        return new ResponseEntity<>(amenities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Amenity> getAmenityById(@PathVariable int id) {
        Amenity amenity = amenityService.getAmenityById(id);
        return ResponseEntity.ok(amenity);
    }

    @PostMapping
    public ResponseEntity<Amenity> createAmenity(@RequestBody @Valid AmenityDTO amenityDTO) {
        Amenity amenity = new Amenity(0,amenityDTO.getName(), amenityDTO.getDescription());
        Amenity createdAmenity = amenityService.createAmenity(amenity);
        return new ResponseEntity<>(createdAmenity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Amenity> updateAmenity(@PathVariable int id, @RequestBody AmenityDTO updatedAmenityDTO) {
        Amenity updatedAmenity = new Amenity(id, updatedAmenityDTO.getName(), updatedAmenityDTO.getDescription());
        Amenity updatedAmenityResult = amenityService.updateAmenity(id, updatedAmenity);
        return ResponseEntity.ok(updatedAmenityResult);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable int id) {
        amenityService.deleteAmenity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT) ;
    }
}
