package com.reisparadijs.reisparadijs.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reisparadijs.reisparadijs.business.domain.Amenity;
import com.reisparadijs.reisparadijs.business.domain.ParkLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 8 Augustus 2024 - 11:00
 */

@Repository
public class JdbcParkLocationDAO implements GenericCrudDao<ParkLocation> {
    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(JdbcParkLocationDAO.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcParkLocationDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        logger.info("JdbcParkLocationDAO initialized");
    }

    public class MemberRowMapper implements RowMapper<ParkLocation> {

        @Override
        public ParkLocation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            int id = resultSet.getInt("id");
            String zipcode = resultSet.getString("zipcode");
            String number = resultSet.getString("number");
            String description = resultSet.getString("description");
            String name = resultSet.getString("name");
            ParkLocation parkLocation = new ParkLocation(id, zipcode, number, description, name);
            List<Amenity> amenities = findAmenitiesByParkLocationId(id);
            for (Amenity amenity : amenities) {
                parkLocation.addAmenity(amenity);
            }

            return parkLocation;
        }
    }


    @Override
    public List<ParkLocation> findAll() {
        List<ParkLocation> parkLocationsAll = jdbcTemplate.query("SELECT * FROM park_location", new JdbcParkLocationDAO.MemberRowMapper());
        return parkLocationsAll;
    }


    @Override
    public Optional<ParkLocation> findById(int id) {
        List<ParkLocation> parkLocationsById = jdbcTemplate.query("SELECT * FROM park_location WHERE id = ?", new JdbcParkLocationDAO.MemberRowMapper(), id);
        if (parkLocationsById.size() != 1) {
            return Optional.empty();
        } else {
            return Optional.of(parkLocationsById.get(0));
        }
    }

    public Optional<ParkLocation> getByName(String name) {
        List<ParkLocation> parkLocationsByName = jdbcTemplate.query("SELECT * FROM park_location WHERE name = ?", new JdbcParkLocationDAO.MemberRowMapper(), name);
        if (parkLocationsByName.size() != 1) {
            return Optional.empty();
        } else {
            return Optional.of(parkLocationsByName.get(0));
        }
    }

    @Override
    public ParkLocation save(ParkLocation parkLocation) {
        // if (checkExistingParkLocation(parkLocation)){
        //    logger.warn("De opgegeven parklocatie bestaat al");
        //    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "De opgegeven parklocatie bestaat al");
      // }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            String sql = "INSERT INTO park_location (zipcode, number, description, name) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, parkLocation.getZipcode());
            ps.setString(2, parkLocation.getNumber());
            ps.setString(3, parkLocation.getDescription());
            ps.setString(4, parkLocation.getName());
            return ps;
        }, keyHolder);
        int newId = keyHolder.getKey().intValue();
        parkLocation.setParkID(newId);
        logger.info("De parklocatie is opgeslagen met ID: {}", newId);
        saveAmenities(parkLocation);
        List<Amenity> amenitiesForParkLocation = findAmenitiesByParkLocationId(newId);
        parkLocation.setParkAmenities(amenitiesForParkLocation);
        return parkLocation;
    }


    public boolean checkExistingParkLocation (ParkLocation parkLocation) {
        String sql = "SELECT COUNT(*) FROM park_location WHERE zipcode = ? AND number = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, parkLocation.getZipcode(), parkLocation.getNumber());
        return count != null && count > 0;
    }

    public boolean checkExistingParkLocationById(int id) {
        String sql = "SELECT COUNT(*) FROM park_location WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count == 1;
    }

    public boolean checkExistingAmenity (Amenity amenity) {
        String checkSql = "SELECT COUNT(*) FROM amenities WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, amenity.getId());
        return count != null && count > 0;
    }


    public void update(ParkLocation parkLocation) {
            String updateSql = "UPDATE park_location SET zipcode = ?, number = ?, description = ?, name = ? WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(updateSql, parkLocation.getZipcode(), parkLocation.getNumber(), parkLocation.getDescription(), parkLocation.getName(), parkLocation.getParkID());
            if (rowsAffected > 0) {
                logger.info("Parklocatie met ID: {} is ge-update", parkLocation.getParkID());
                deleteAmenitiesByParkLocationId(parkLocation.getParkID()); // n.b.: first delete all amenities and then re-save all amenities for Front End check-box (deselect en select)
                saveAmenities(parkLocation);
                List<Amenity> amenitiesForParkLocation = findAmenitiesByParkLocationId(parkLocation.getParkID());
                parkLocation.setParkAmenities(amenitiesForParkLocation);
            } else {
                logger.warn("Parklocatie kan niet worden ge-update voor parklocatie met ID: {}", parkLocation.getParkID());
            }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM park_location WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected > 0) {
            deleteAmenitiesByParkLocationId(id);
            logger.info("Parklocatie met ID: {} is verwijderd", id);
        } else {
            logger.warn("Geen parklocaties gevonden met ID: {}", id);
        }
    }

    protected void saveAmenities(ParkLocation parkLocation) {
        if (parkLocation.getParkAmenities() != null) {
            String insertSql = "INSERT INTO amenities_park_location (amenities_id, park_location_id) VALUES (?, ?)";
            for (Amenity amenity : parkLocation.getParkAmenities()) {
                    if (checkExistingAmenity(amenity)) {
                        jdbcTemplate.update(insertSql, amenity.getId(), parkLocation.getParkID());
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amenity met ID " + amenity.getId() + " bestaat niet");
                    }
                }
        } else {
            logger.warn("Geen amenities om op te slaan voor Parklocatie met ID={}", parkLocation.getParkID());
        }
    }



    private void deleteAmenitiesByParkLocationId(int parkLocationId) {
        String sql = "DELETE FROM amenities_park_location WHERE park_location_id = ?";
        jdbcTemplate.update(sql, parkLocationId);
    }

    private List<Amenity> findAmenitiesByParkLocationId(int parkLocationId) {
        String sql = "SELECT a.id, a.name, a.description FROM amenities AS a " +
                "JOIN amenities_park_location AS apl ON a.id = apl.amenities_id " +
                "WHERE apl.park_location_id = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            int amenityId = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            return new Amenity(amenityId, name, description);
        }, parkLocationId);
    }

}
