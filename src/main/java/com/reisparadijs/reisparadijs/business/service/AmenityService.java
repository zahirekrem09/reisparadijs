package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.Amenity;
import com.reisparadijs.reisparadijs.persistence.repository.AmenityRepository;
import com.reisparadijs.reisparadijs.utilities.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;

    @Autowired
    public AmenityService(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    public Amenity getAmenityById(int id) {
        return amenityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Amenity with ID " + id + " not found."));
    }

    public Amenity createAmenity(Amenity amenity) {
        amenityRepository.save(amenity);
        return amenity;
    }

    public Amenity updateAmenity(int id, Amenity updatedAmenity) {
        Amenity existingAmenity = amenityRepository.findById(id).orElseThrow(() -> new NotFoundException("Amenity with ID " + id + " not found."));
        existingAmenity.setName(updatedAmenity.getName());
        existingAmenity.setDescription(updatedAmenity.getDescription());
        amenityRepository.update(existingAmenity);
        return existingAmenity;

    }

    public void deleteAmenity(int id) {
        Amenity existingAmenity = amenityRepository.findById(id).orElseThrow(() -> new NotFoundException("Amenity with ID " + id + " not found."));
            amenityRepository.delete(id); }

}
