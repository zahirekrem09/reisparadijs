package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.Image;
import com.reisparadijs.reisparadijs.persistence.dao.ImageDAO;
import com.reisparadijs.reisparadijs.persistence.dao.JdbcImageDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ImageRepository {
    private final Logger logger = LoggerFactory.getLogger(ImageRepository.class);
    private final ImageDAO jdbcImageDAO;

    @Autowired
    public ImageRepository(JdbcImageDAO jdbcImageDAO) {
        super();
        this.jdbcImageDAO = jdbcImageDAO;
        logger.info("Image Repository created");
    }

    // Methode om een afbeelding te vinden op basis van ID
    public Optional<Image> findById(int id) {
        Optional<Image> image = jdbcImageDAO.findById(id);
        return image;
    }

    // Methode om alle afbeeldingen op te halen
    public List<Image> findAll() {
        List<Image> images = jdbcImageDAO.findAll();
        return images;
    }

    // Methode om een nieuwe afbeelding op te slaan
    public void save(Image image) {
        jdbcImageDAO.save(image);
    }

    // Methode om een bestaande afbeelding bij te werken
    public void update(Image image) {
        jdbcImageDAO.update(image);
    }

    // Methode om een afbeelding te verwijderen op basis van ID
    public void delete(int id) {
        jdbcImageDAO.delete(id);
    }
}
