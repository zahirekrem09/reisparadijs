package com.reisparadijs.reisparadijs.communication.dto.request;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;

public class ImageDTO {
    private String imageBase64; // Base64-gecodeerde string die de afbeelding vertegenwoordigt
    private int accommodationId; // De ID van de accommodatie waarmee de afbeelding is geassocieerd

    // Getters en Setters
    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public int getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(int accommodationId) {
        this.accommodationId = accommodationId;
    }
}
