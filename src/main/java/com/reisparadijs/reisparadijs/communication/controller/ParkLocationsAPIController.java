package com.reisparadijs.reisparadijs.communication.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reisparadijs.reisparadijs.business.domain.ParkLocation;
import com.reisparadijs.reisparadijs.business.service.ParkLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 8 Augustus 2024 - 12:30
 */

@CrossOrigin(origins = "http://127.0.0.1:5500") // Specifieke origin toestaan
@RestController
@RequestMapping("/api/parklocations")
public class ParkLocationsAPIController {

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(ParkLocationsAPIController.class);
    private final ParkLocationService parkLocationService;

    @Autowired
    public ParkLocationsAPIController(ParkLocationService parkLocationService) {
        this.parkLocationService = parkLocationService;
        logger.info("ParkLocations API controller created");
    }

    @GetMapping()
    public ResponseEntity<List<ParkLocation>> getAllParkLocations() {
        return ResponseEntity.ok(parkLocationService.findAll());
    }

    @PostMapping()
    public ResponseEntity<?> saveParkLocation(@RequestBody ParkLocation parkLocation) {
        if (parkLocationService.checkExistingParkLocation(parkLocation)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Deze parklocatie bestaat al! Postcode: " + parkLocation.getZipcode() + " Huisnummer: " + parkLocation.getNumber());
        }
        parkLocationService.save(parkLocation);
        return ResponseEntity.ok(parkLocation);
    }

    @DeleteMapping("/{parkID}")
    public ResponseEntity<Optional<ParkLocation>> deleteParkLocation(@PathVariable int parkID) {
        parkLocationService.deleteOneById(parkID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{parkID}")
    public ResponseEntity<ParkLocation> getParkLocationByID(@PathVariable int parkID) {
        return ResponseEntity.ok(parkLocationService.findOneById(parkID));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateParkLocation(@PathVariable int id, @RequestBody ParkLocation parkLocation) {
        if (id != parkLocation.getParkID()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Park Location ID in URL and request body do not match");
        }
        if (!parkLocationService.checkExistingParkLocationByID(parkLocation.getParkID())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Geen bestaande parklocatie met ID: " + parkLocation.getParkID());
        }
        parkLocationService.update(parkLocation);
        return ResponseEntity.ok(parkLocation);
    }
}
