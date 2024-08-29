package com.reisparadijs.reisparadijs.business.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reisparadijs.reisparadijs.business.domain.ParkLocation;
import com.reisparadijs.reisparadijs.persistence.repository.ParkLocationRepository;
import com.reisparadijs.reisparadijs.utilities.exceptions.AlreadyExistsException;
import com.reisparadijs.reisparadijs.utilities.exceptions.BadCredentialsException;
import com.reisparadijs.reisparadijs.utilities.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 8 Augustus 2024 - 12:30
 */

@Service
public class ParkLocationService {
    private final ParkLocationRepository parkLocationRepository;

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(ParkLocationService.class);

    @Autowired
    public ParkLocationService(ParkLocationRepository parkLocationRepository) {
        super();
        this.parkLocationRepository = parkLocationRepository;
        logger.info("ParkLocation Service created");
    }

    public ParkLocation save(ParkLocation parkLocation){
        return parkLocationRepository.save(parkLocation);}

    public void update(ParkLocation parkLocation){
        parkLocationRepository.update(parkLocation);
    }

   public void deleteOneById(int id){
       ParkLocation parkLocationToDelete = findOneById(id);
        parkLocationRepository.deleteOneById(parkLocationToDelete.getParkID());
   }

   public ParkLocation findOneById(int id){
        return parkLocationRepository.getById(id).orElseThrow(() -> new NotFoundException("Geen parklocatie gevonden met deze ID"));
   }


    public List<ParkLocation> findAll() {
        return parkLocationRepository.getAll();
    }

    public boolean checkExistingParkLocationByID(int id){
        return parkLocationRepository.checkExistingParkLocationByID(id);
    }

    public boolean checkExistingParkLocation(ParkLocation parkLocation){
        return parkLocationRepository.checkExistingParkLocation(parkLocation);
    }

}
