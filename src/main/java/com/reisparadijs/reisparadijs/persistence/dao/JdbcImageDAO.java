package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.Image;
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
public class JdbcImageDAO implements ImageDAO {

    // Logger voor het loggen van informatie en fouten
    private final Logger logger = LoggerFactory.getLogger(JdbcImageDAO.class);

    // JdbcTemplate voor interactie met de database
    private final JdbcTemplate jdbcTemplate;

    // Constructor die JdbcTemplate injecteert via dependency injection
    @Autowired
    public JdbcImageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("JdbcImageDAO initialized");
    }

    // Inner class die gebruikt wordt om ResultSet-gegevens naar Image-objecten te mappen
    private static class ImageRowMapper implements RowMapper<Image> {

        // Deze methode mapt de huidige rij van het ResultSet naar een Image-object
        @Override
        public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
            Image image = new Image();
            image.setId(rs.getInt("id")); // Zet de ID van de afbeelding
            image.setImage(rs.getBlob("image")); // Zet de Blob van de afbeelding
            // Zorg ervoor dat je ook de accommodatie-informatie ophaalt en instelt als dat nodig is
            // Bijvoorbeeld: image.setAccommodation(new Accommodation(rs.getInt("accommodation_id")));
            return image;
        }
    }

    // Methode om een nieuwe afbeelding op te slaan in de database
    @Override
    public Image save(Image image) {
        String query = "INSERT INTO image (image, accommodation_id) VALUES (?, ?)";
        jdbcTemplate.update(query, image.getImage(), image.getAccommodation().getId());
        Integer generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        image.setId(generatedId);
        logger.info("Saved new Image: {}", image); // Log een bericht wanneer een nieuwe afbeelding is opgeslagen
        return image;
    }

    // Methode om een afbeelding op te zoeken op basis van het ID
    @Override
    public Optional<Image> findById(int id) { // Verander de parameter naar long
        String query = "SELECT * FROM image WHERE id = ?";
        List<Image> images = jdbcTemplate.query(query, new ImageRowMapper(), id);
        if (images.size() != 1) {
            return Optional.empty(); // Retourneer een lege Optional als er geen of meerdere afbeeldingen zijn gevonden
        } else {
            return Optional.of(images.get(0)); // Retourneer de gevonden afbeelding in een Optional
        }
    }

    @Override
    public List<Image> findAll() {
        String query = "SELECT * FROM image";
        return jdbcTemplate.query(query, new ImageRowMapper()); // Voer de query uit en map de resultaten naar een lijst van afbeeldingen

    }


    // Methode om een bestaande afbeelding bij te werken in de database
    @Override
    public void update(Image image) {
        String query = "UPDATE image SET image = ?, accommodation_id = ? WHERE id = ?";
        jdbcTemplate.update(query, image.getImage(), image.getAccommodation().getId(), image.getId());
        logger.info("Updated Image: {}", image); // Log een bericht wanneer een afbeelding is bijgewerkt
    }


    // Methode om een afbeelding te verwijderen op basis van het ID
    @Override
    public void delete(int id) { // Verander de parameter naar long
        String query = "DELETE FROM image WHERE id = ?";
        jdbcTemplate.update(query, id);
        logger.info("Deleted Image with ID: {}", id); // Log een bericht wanneer een afbeelding is verwijderd
    }
}
