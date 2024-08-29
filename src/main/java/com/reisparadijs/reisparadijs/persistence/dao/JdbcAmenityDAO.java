package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.Amenity;
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

@Repository
public class JdbcAmenityDAO  implements AmenityDAO {

    // Logger voor het loggen van informatie en fouten
    private final Logger logger = LoggerFactory.getLogger(JdbcAmenityDAO.class);

    // JdbcTemplate voor interactie met de database
    private final JdbcTemplate jdbcTemplate;

    // Constructor die JdbcTemplate injecteert via dependency injection
    @Autowired
    public JdbcAmenityDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("JdbcAmenityDAO initialized");
    }

    // Inner class die gebruikt wordt om ResultSet-gegevens naar Amenity-objecten te mappen
    private static class AmenityRowMapper implements RowMapper<Amenity> {

        // Deze methode mapt de huidige rij van het ResultSet naar een Amenity-object
        @Override
        public Amenity mapRow(ResultSet rs, int rowNum) throws SQLException {
            Amenity amenity = new Amenity();
            amenity.setId(rs.getInt("id")); // Zet de ID van de voorziening
            amenity.setName(rs.getString("name")); // Zet de naam van de voorziening
            amenity.setDescription(rs.getString("description")); // Zet de beschrijving van de voorziening
            return amenity;
        }
    }

    // Methode om een nieuwe voorziening op te slaan in de database
    @Override
    public Amenity save(Amenity amenity) {
        String query = "INSERT INTO amenities (name, description) VALUES (?, ?)";
        jdbcTemplate.update(query, amenity.getName(), amenity.getDescription());
        Integer generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        amenity.setId(generatedId);
        logger.info("Saved new Amenity: {}", amenity); // Log een bericht wanneer een nieuwe voorziening is opgeslagen
        return amenity;
    }

    // Methode om een voorziening op te zoeken op basis van het ID
    @Override
    public Optional<Amenity> findById(int id) {
        String query = "SELECT * FROM amenities WHERE id = ?";
        List<Amenity> amenities = jdbcTemplate.query(query, new AmenityRowMapper(), id);
        // Controleer of de lijst met voorzieningen leeg is of meer dan één resultaat heeft
        if (amenities.size() != 1) {
            return Optional.empty(); // Retourneer een lege Optional als er geen of meerdere voorzieningen zijn gevonden
        } else {
            return Optional.of(amenities.get(0)); // Retourneer de gevonden voorziening in een Optional
        }
    }

    // Methode om alle voorzieningen op te halen uit de database
    @Override
    public List<Amenity> findAll() {
        String query = "SELECT * FROM amenities";
        return jdbcTemplate.query(query, new AmenityRowMapper()); // Voer de query uit en map de resultaten naar een lijst van voorzieningen
    }


    // Methode om een bestaande voorziening bij te werken in de database
    public void update(Amenity amenity) {
        String query = "UPDATE amenities SET name = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(query, amenity.getName(), amenity.getDescription(), amenity.getId());
        logger.info("Updated Amenity: {}", amenity); // Log een bericht wanneer een voorziening is bijgewerkt
    }


    // Methode om een voorziening te verwijderen op basis van het ID
    public void delete(int id) {
        String query = "DELETE FROM amenities WHERE id = ?";
        jdbcTemplate.update(query, id);
        logger.info("Deleted Amenity with ID: {}", id); // Log een bericht wanneer een voorziening is verwijderd
    }
}
