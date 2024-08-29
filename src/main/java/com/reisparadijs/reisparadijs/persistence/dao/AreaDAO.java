package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.Area;

import java.util.List;
import java.util.Optional;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 7 Augustus 2024 - 10:00
 */

public interface AreaDAO {

    Optional<Area> findByZipcodeNumbers(int zipcodeNumber);

    List<Area> getAllAreas();

    List<Area> getAllAreasByLocation(String location);
}
