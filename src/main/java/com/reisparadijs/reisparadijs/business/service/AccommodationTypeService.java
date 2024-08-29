package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.AccommodationType;
import com.reisparadijs.reisparadijs.persistence.repository.AccommodationTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/*********************************************
 * @Author Mirre Cicilia
 * @Studentennummer 500241293
 * @Project Accommodation.java
 * @Created 14 August Wednesday 2024 - 18:21
 * @Korte beschrijving: De AccommodationTypeService bevat de businesslogica van de applicatie. Hij verwerkt de aanvragen
 *  * die van de controller komen. De Service werkt met complete domeinobjecten (dit zijn objecten die alle informatie en
 *  * gedragingen bevatten voor een specifiek onderdeel van de applicatie.
 **********************************************/
@Service
public class AccommodationTypeService {

    private static final Logger logger = LoggerFactory.getLogger(AccommodationTypeService.class);

    private final AccommodationTypeRepository accommodationTypeRepository;

    public AccommodationTypeService(AccommodationTypeRepository accommodationTypeRepository) {
        this.accommodationTypeRepository = accommodationTypeRepository;
        logger.info("New AccommodationTypeService.");
    }

    // alle accommodatie typen ophalen
    public List<AccommodationType> findAll() {
        return accommodationTypeRepository.findAll();
    }
}
