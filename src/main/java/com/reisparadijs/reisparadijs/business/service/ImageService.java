package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.Image;
import com.reisparadijs.reisparadijs.persistence.repository.ImageRepository;
import com.reisparadijs.reisparadijs.utilities.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Image getImageById(int id) {
        return imageRepository.findById(id).orElseThrow(() -> new NotFoundException("Image not found with ID: " + id));
    }

    public Image createImage(Image image) {
        imageRepository.save(image);
        return image;
    }

    public Image updateImage(int id, Image updatedImage) {
        Image existingImage = imageRepository.findById(id).orElseThrow(() -> new NotFoundException("Image not found with ID: " + id));

        existingImage.setImage(updatedImage.getImage());
        existingImage.setAccommodation(updatedImage.getAccommodation());
        imageRepository.save(existingImage); // vergeet niet om het bijgewerkte object op te slaan
        return existingImage;

    }

    public void deleteImage(int id) {
         imageRepository.findById(id).orElseThrow(() -> new NotFoundException("Image not found with ID: " + id));
            imageRepository.delete(id);
    }

    public Blob convertBase64ToBlob(String base64Image) throws SQLException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        return new javax.sql.rowset.serial.SerialBlob(decodedBytes);
    }
}
