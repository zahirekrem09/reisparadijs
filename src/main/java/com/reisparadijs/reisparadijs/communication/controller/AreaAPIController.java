package com.reisparadijs.reisparadijs.communication.controller;


import com.reisparadijs.reisparadijs.business.domain.Area;
import com.reisparadijs.reisparadijs.business.domain.ParkLocation;
import com.reisparadijs.reisparadijs.business.service.AreaService;
import com.reisparadijs.reisparadijs.business.service.ChatGPTService;
import com.reisparadijs.reisparadijs.communication.dto.request.LocationRadiusRequestDTO;
import com.reisparadijs.reisparadijs.communication.dto.response.AreaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 7 Augustus 2024 - 10:00
 */

@RestController
@RequestMapping("/api/areas")
public class AreaAPIController {

    private final Logger logger = LoggerFactory.getLogger(AreaAPIController.class);
    private final AreaService areaService;

    @Autowired
    public AreaAPIController(
            AreaService areaService) {
        this.areaService = areaService;
        logger.info("Area API controller created");
    }

    // @Deprecate

    @GetMapping()
    public ResponseEntity<?> findLocationsByRadius(@RequestBody LocationRadiusRequestDTO locationRadius) {
        String location = locationRadius.getLocation();
        Integer radius = locationRadius.getRadius().orElse(0);

        // Haal de lijst met gebieden op basis van de opgegeven locatie
        List<Area> locationList = areaService.getAllAreasByLocation(location);


        // Controleer of er gebieden zijn gevonden binnen de radius, anders vraag via ChatGPT Service
        if (locationList.isEmpty()) {
            String altLocation = ChatGPTService.ChatGPTLocationRequest(location);
            locationList = areaService.getAllAreasByLocation(altLocation);

            if (locationList.isEmpty()) {
                String message = "Locatie " + location + " is geen bekende locatie, probeer een andere locatie in Nederland";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
            }
        }
        // Haal alle bestaande Postcodes op die in de Tabel AREAs staan
        List<Area> zipcodeList = areaService.findAll();

        // Zoek alle gebieden binnen de opgegeven locatie en de opgegeven radius
        List<Area> locationsWithinRadius = AreaService.findLocationsWithinRadius(zipcodeList, locationList, radius);

        // Map de gevonden gebieden naar AreaDTO en geef ze terug
        List<AreaDTO> result = locationsWithinRadius.stream()
                .map(resultArea -> new AreaDTO(resultArea.getZipcodeNumbers()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCities() {
        return ResponseEntity.ok(areaService.getAllCities());
    }

    @GetMapping("/provinces")
    public ResponseEntity<List<String>> getAllProvinces() {
        return ResponseEntity.ok(areaService.getAllProvinces());
    }

    @GetMapping("/taggedAreas")
    public ResponseEntity<List<String>> getAllTaggedAreas() {
        return ResponseEntity.ok(areaService.getAllTaggedAreas());
    }



}
