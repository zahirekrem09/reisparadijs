package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.communication.dto.request.LocationRadiusRequestDTO;
import com.reisparadijs.reisparadijs.communication.dto.request.SearchRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 14 August Wednesday 2024 - 11:48
 */

@Service
public class SearchService {

    private static final SimpleDateFormat formatter
             = new SimpleDateFormat("yyyy-MM-dd");

    private final AreaService areaService;
    private final AccommodationService accommodationService;

    public SearchService(AreaService areaService, AccommodationService accommodationService) {
        this.areaService = areaService;

        this.accommodationService = accommodationService;
    }

    public List<Accommodation> search(SearchRequest request) throws ParseException {
        // stap : 1 find area: return list of zipcodes
        List<String> areas = areaService
                .findLocationsByRadius(new LocationRadiusRequestDTO(request.location(), 0)).stream()
                .map(zp -> String.valueOf(zp.getZipcodeNumbers())).toList();
        // check areas is not empty
        if (areas.isEmpty()) {
            return new ArrayList<>();
        }
        // stap : 2-a find Accommodations met numberOfGuests. return list van Accommodation
        // stap : 2-b find Check areas met list Accommodation met zipCode(met eerste 4 cijfers)
        // stap : 2-c return list Accommodation
        List<Accommodation> accommodations = accommodationService
                .findAccommodationsByNumberOfGuests(request.numberOfGuests())
                .stream()
                .filter(accommodation -> areas.contains(accommodation.getZipCode().substring(0, 4)))
                .toList();
        // stap : 3 check if list Accommodation is Available
        List<Integer> accommodationIds = accommodationService
                .findAllAvailableAccommodationIds( formatter.parse(request.checkinDate()),formatter.parse(request.checkoutDate()) );

        // fixme : stap-3 en step-4 should be in one method
        // stap : 4 return list Accommodation
        return accommodations.stream()
                .filter(accommodation -> accommodationIds.contains(accommodation.getId()))
                .collect(Collectors.toList());

    }
}
