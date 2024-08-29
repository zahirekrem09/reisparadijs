package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.Area;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 7 Augustus 2024 - 10:00
 */

@Repository
public class JdbcAreaDAO implements AreaDAO {

    private final Logger logger = LoggerFactory.getLogger(JdbcAreaDAO.class);
    private final JdbcTemplate jdbcTemplate;

@Autowired
public JdbcAreaDAO(JdbcTemplate jdbcTemplate) {
    super();
    this.jdbcTemplate = jdbcTemplate;
    logger.info("JdbcPostcodeDAO initialized");
}


    private static class MemberRowMapper implements RowMapper<Area> {

        @Override
        public Area mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            int zipcodeNumbers = resultSet.getInt("zipcodeNumbers");
            double zcLat = resultSet.getDouble("zcLat");
            double zcLon = resultSet.getDouble("zcLon");
            Area zipcode = new Area(zipcodeNumbers, zcLat, zcLon);
            return zipcode;
        }
    }

    @Override
    public Optional<Area> findByZipcodeNumbers(int zipcodeNumbers) {
        List<Area> zipcodes = jdbcTemplate.query("SELECT zipcodeNumbers, zcLat, zcLon FROM area WHERE zipcodeNumbers = ?", new MemberRowMapper(), zipcodeNumbers);
        if (zipcodes.size() != 1) {
            return Optional.empty();
        } else {
            return Optional.of(zipcodes.get(0));
        }
    }

    @Override
    public List<Area> getAllAreas() {
        List<Area> allAreas = jdbcTemplate.query("SELECT zipcodeNumbers, zcLat, zcLon FROM area", new MemberRowMapper());
        return allAreas;
    }

    @Override
    public List<Area> getAllAreasByLocation(String location) {
        String sql = "SELECT zipcodeNumbers, zcLat, zcLon FROM area WHERE place = ? OR municipality = ? OR province = ? OR Area_1 = ? OR Area_2 = ? OR Area_3 = ?";
        return jdbcTemplate.query(sql, new MemberRowMapper(), location, location, location, location, location, location);
    }

    public List<String> getAllCities() {
        String sql = "SELECT DISTINCT place FROM area";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("place"));
    }

    public List<String> getAllProvinces() {
        String sql = "SELECT DISTINCT province FROM area";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("province"));
    }

    public List<String> getAllTaggedAreas() {
        String sql = "SELECT DISTINCT Area_1 AS area FROM area UNION SELECT DISTINCT Area_2 FROM area UNION SELECT DISTINCT Area_3 FROM area";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("area"));
    }

}
