package com.reisparadijs.reisparadijs.persistence.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reisparadijs.reisparadijs.business.domain.ParkLocation;
import com.reisparadijs.reisparadijs.persistence.dao.AreaDAO;
import com.reisparadijs.reisparadijs.persistence.dao.JdbcAreaDAO;
import com.reisparadijs.reisparadijs.persistence.dao.JdbcParkLocationDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 8 Augustus 2024 - 12:00
 */

@Repository
public class ParkLocationRepository {
    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(ParkLocationRepository.class);
    private final JdbcParkLocationDAO jdbcParkLocationDAO;

    @Autowired
    public ParkLocationRepository(JdbcParkLocationDAO jdbcParkLocationDAO) {
        super();
        this.jdbcParkLocationDAO = jdbcParkLocationDAO;
        logger.info("Park_Location Repository created");
    }

    public List<ParkLocation> getAll() {
        return jdbcParkLocationDAO.findAll();
    }

    public Optional<ParkLocation> getById(int id) {
        return jdbcParkLocationDAO.findById(id);
    }

    public Optional<ParkLocation> getByName(String name){
        return jdbcParkLocationDAO.getByName(name);
    }

    public ParkLocation save(ParkLocation parkLocation){
        jdbcParkLocationDAO.save(parkLocation);
        return parkLocation;
    }

    public void update(ParkLocation parkLocation){
        jdbcParkLocationDAO.update(parkLocation);
    }

    public void deleteOneById(int id){
        jdbcParkLocationDAO.delete(id);
    }

    public boolean checkExistingParkLocation(ParkLocation parkLocation){
        return jdbcParkLocationDAO.checkExistingParkLocation(parkLocation);
    }

    public boolean checkExistingParkLocationByID(int id){
        return jdbcParkLocationDAO.checkExistingParkLocationById(id);
    }
}
