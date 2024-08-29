package com.reisparadijs.reisparadijs.communication.controller;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.Image;
import com.reisparadijs.reisparadijs.business.service.AccommodationService;
import com.reisparadijs.reisparadijs.business.service.ImageService;
import com.reisparadijs.reisparadijs.communication.dto.request.ImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;
    private final AccommodationService accommodationService;

    @Autowired
    public ImageController(ImageService imageService, AccommodationService accommodationService) {
        this.imageService = imageService;
        this.accommodationService = accommodationService;
        logger.info("New ImageController initialized.");
    }

    @PostMapping
    public ResponseEntity<Image> createImage(@RequestBody @Valid ImageDTO imageDTO) throws SQLException {
            Accommodation accommodation = accommodationService.findById(imageDTO.getAccommodationId());
            Blob imageBlob = imageService.convertBase64ToBlob(imageDTO.getImageBase64());
            Image image = new Image();
            image.setImage(imageBlob);
            image.setAccommodation(accommodation);
            Image createdImage = imageService.createImage(image);
            return new ResponseEntity<>(createdImage, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable int id) {
        Image image = imageService.getImageById(id);
        return ResponseEntity.ok(image);
    }

    @GetMapping
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Image> updateImage(@PathVariable int id, @RequestBody @Valid ImageDTO updatedImageDTO) throws SQLException {
            Accommodation accommodation = accommodationService.findById(updatedImageDTO.getAccommodationId());
            Blob imageBlob = imageService.convertBase64ToBlob(updatedImageDTO.getImageBase64());
            Image updatedImage = new Image();
            updatedImage.setId(id);
            updatedImage.setImage(imageBlob);
            updatedImage.setAccommodation(accommodation);

           imageService.updateImage(id, updatedImage);
            return  ResponseEntity.ok(updatedImage);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable int id) {
        imageService.deleteImage(id);
        return  ResponseEntity.noContent().build();
    }
}
