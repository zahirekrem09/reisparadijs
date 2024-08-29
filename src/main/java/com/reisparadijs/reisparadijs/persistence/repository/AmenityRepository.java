package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.Amenity;
import com.reisparadijs.reisparadijs.persistence.dao.AmenityDAO;
import com.reisparadijs.reisparadijs.persistence.dao.JdbcAmenityDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AmenityRepository {
    private final Logger logger = LoggerFactory.getLogger(AmenityRepository.class);
    private final AmenityDAO jdbcAmenityDAO;

    @Autowired
    public AmenityRepository(JdbcAmenityDAO jdbcAmenityDAO) {
        super();
        this.jdbcAmenityDAO = jdbcAmenityDAO;
        logger.info("Amenity Repository created");
    }

    // Methode om een voorziening te vinden op basis van ID
    public Optional<Amenity> findById(int id) {
        Optional<Amenity> amenity = jdbcAmenityDAO.findById(id);
        return amenity;
    }

    // Methode om alle voorzieningen op te halen
    public List<Amenity> findAll() {
        List<Amenity> amenities = jdbcAmenityDAO.findAll();
        return amenities;
    }

    // Methode om een nieuwe voorziening op te slaan
    public void save(Amenity amenity) {
        jdbcAmenityDAO.save(amenity);
        logger.info("Saved new Amenity: {}", amenity);
    }

    // Methode om een bestaande voorziening bij te werken
    public void update(Amenity amenity) {
        jdbcAmenityDAO.update(amenity);
        logger.info("Updated Amenity: {}", amenity);
    }

    // Methode om een voorziening te verwijderen op basis van ID
    public void delete(int id) {
        jdbcAmenityDAO.delete(id);
        logger.info("Deleted Amenity with ID: {}", id);
    }
}
