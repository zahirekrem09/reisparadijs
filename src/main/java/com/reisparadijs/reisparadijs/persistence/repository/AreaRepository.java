package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.Area;
import com.reisparadijs.reisparadijs.persistence.dao.JdbcAreaDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 7 Augustus 2024 - 10:00
 */

@Repository
public class AreaRepository {
    private final Logger logger = LoggerFactory.getLogger(AreaRepository.class);
    private final JdbcAreaDAO jdbcAreaDAO;

    @Autowired
    public AreaRepository(JdbcAreaDAO jdbcAreaDAO) {
        super();
        this.jdbcAreaDAO = jdbcAreaDAO;
        logger.info("Area Repository created");
    }

    public Optional<Area> findByZipcodeNumbers(int zipcodeNumber){
        Optional<Area> zipCodes = jdbcAreaDAO.findByZipcodeNumbers(zipcodeNumber);
        return zipCodes;
    }

    public List<Area> findAll(){
        List<Area> areaList = jdbcAreaDAO.getAllAreas();
        return areaList;
    }

    public List<Area> getAllAreasByLocation(String location) {
        List<Area> allLocations = jdbcAreaDAO.getAllAreasByLocation(location);
        return allLocations;
    }

    public List<String> getAllCities(){
        return jdbcAreaDAO.getAllCities();
    }

    public List<String> getAllProvinces(){
        return jdbcAreaDAO.getAllProvinces();
    }

    public List<String> getAllTaggedAreas(){
        return jdbcAreaDAO.getAllTaggedAreas();
    }

}
