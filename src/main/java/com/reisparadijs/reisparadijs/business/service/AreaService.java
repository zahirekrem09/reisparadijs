package com.reisparadijs.reisparadijs.business.service;


import com.reisparadijs.reisparadijs.business.domain.Area;
import com.reisparadijs.reisparadijs.communication.dto.request.LocationRadiusRequestDTO;
import com.reisparadijs.reisparadijs.communication.dto.response.AreaDTO;
import com.reisparadijs.reisparadijs.persistence.repository.AreaRepository;
import com.reisparadijs.reisparadijs.utilities.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 7 Augustus 2024 - 10:00
 */

@Service
public class AreaService {
    private final AreaRepository areaRepository;

    private final Logger logger = LoggerFactory.getLogger(AreaService.class);

    @Autowired
    public AreaService(AreaRepository areaRepository) {
        super();
        this.areaRepository = areaRepository;
        logger.info("AreaService created");
    }

    public Area findByZipcodeNumbers(int zipcodeNumbers) {
        Optional<Area> optionalPostcode = areaRepository.findByZipcodeNumbers(zipcodeNumbers);
        return optionalPostcode.orElse(null);
    }

    public List<Area> getAllAreasByLocation(String location) {
        List<Area> allLocations = areaRepository.getAllAreasByLocation(location);
        return allLocations;
    }

    public List<Area> findAll() {
        return areaRepository.findAll();
    }

    // method calculates the distance (Radius) between two given points based on the Longitude and Latitude of these two points.
    // in this method the Lon and Lat of two Zipcodes are used to calculate the distance between them
    public static double calculateDistance(Area zipcode2, Area zipcode1) {
        final int EARTH_RADIUS = 6371; // Earth Radius in KMs
        double lat1 = zipcode1.getZcLat();
        double lat2 = zipcode2.getZcLat();
        double lon1 = zipcode1.getZcLon();
        double lon2 = zipcode2.getZcLon();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    // method returns a list of ZipcodesNumbers within a given radius of the provided zipcode (zipcodeBase)
    public static List<Area> findZipcodesWithinRadius(List<Area> zipcodesAll, Area zipcodeBase, int radius) {
        List<Area> foundAreas = new ArrayList<>();
        for (Area zipcode : zipcodesAll) {
            double distance = calculateDistance(zipcodeBase, zipcode);
            if (distance <= radius) {
                foundAreas.add(zipcode);
            }
        }
        return foundAreas;
    }

    // method returns a list of ZipcodesNumbers within a given radius of the provided Area
    // method checks the provided Zipcode list with all Zipcodes against the Zipcode list within the Provided Area, if Zipcode is within provided Radius Zipcode is added to resultList
    public static List<Area> findLocationsWithinRadius(List<Area> zipcodesAll, List<Area> locations, int radius) {
        Set<Area> resultAreasSet = new TreeSet<>();

        for (Area zipcode1 : zipcodesAll) {
            for (Area zipcode2 : locations) {
                double distance = calculateDistance(zipcode1, zipcode2);
                if (distance <= radius) {
                    resultAreasSet.add(zipcode1);
                    break;
                }
            }
        }
        return new ArrayList<>(resultAreasSet);
    }

    public List<AreaDTO> findLocationsByRadius(LocationRadiusRequestDTO locationRadius) {
        String location = locationRadius.getLocation();
        Integer radius = locationRadius.getRadius().orElse(0);
        // Haal de lijst met gebieden op basis van de opgegeven locatie
        List<Area> locationList = getAllAreasByLocation(location);
        // Controleer of er gebieden zijn gevonden binnen de radius, anders vraag via ChatGPT Service
        if (locationList.isEmpty()) {
            String altLocation = ChatGPTService.ChatGPTLocationRequest(location);
            locationList = getAllAreasByLocation(altLocation);

            if (locationList.isEmpty()) {
                String message = "Locatie " + location + " is geen bekende locatie, probeer een andere locatie in Nederland";
               throw  new NotFoundException(message);
            }
        }
        // Haal alle bestaande Postcodes op die in de Tabel AREAs staan
        List<Area> zipcodeList = findAll();
        // Zoek alle gebieden binnen de opgegeven locatie en de opgegeven radius
        List<Area> locationsWithinRadius = findLocationsWithinRadius(zipcodeList, locationList, radius);
        // Map de gevonden gebieden naar AreaDTO en geef ze terug

        return locationsWithinRadius.stream()
                .map(resultArea -> new AreaDTO(resultArea.getZipcodeNumbers()))
                .collect(Collectors.toList());
    }

    public List<String> getAllCities(){
        return areaRepository.getAllCities();
    }

    public List<String> getAllProvinces(){
        return areaRepository.getAllProvinces();
    }

    public List<String> getAllTaggedAreas(){
        return areaRepository.getAllTaggedAreas();
    }


}
