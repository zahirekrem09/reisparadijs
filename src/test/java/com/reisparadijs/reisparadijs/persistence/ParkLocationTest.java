package com.reisparadijs.reisparadijs.persistence;

import com.reisparadijs.reisparadijs.business.domain.Amenity;
import com.reisparadijs.reisparadijs.business.domain.ParkLocation;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 13 Augustus 2024 - 17:00
 */


// NB nu nog alleen drie verschillende constructors in test, verder aanvullen voor DAO etc.
// check how to
public class ParkLocationTest {

    @Test
    void testConstructorWithAllFields() {
        List<Amenity> amenities = new ArrayList<>();
        amenities.add(new Amenity(1, "Zwembad", "Groot zwembad"));

        ParkLocation parkLocation = new ParkLocation(1, "1019PS", "591", "Robs huis in Amsterdam", "Robs huis", amenities);

        assertEquals(1, parkLocation.getParkID());
        assertEquals("1019PS", parkLocation.getZipcode());
        assertEquals("591", parkLocation.getNumber());
        assertEquals("Robs huis in Amsterdam", parkLocation.getDescription());
        assertEquals("Robs huis", parkLocation.getName());
        assertEquals(amenities, parkLocation.getParkAmenities());
    }

    @Test
    void testConstructorZonderAmenities() {
        ParkLocation parkLocation = new ParkLocation(2, "1019PS", "592", "Robs huis in Amsterdam 2", "Robs huis");

        assertEquals(2, parkLocation.getParkID());
        assertEquals("1019PS", parkLocation.getZipcode());
        assertEquals("592", parkLocation.getNumber());
        assertEquals("Robs huis in Amsterdam 2", parkLocation.getDescription());
        assertEquals("Robs huis", parkLocation.getName());
        assertNull(parkLocation.getParkAmenities());
    }

    @Test
    void testLegeConstructor() {
        ParkLocation parkLocation = new ParkLocation();

        assertEquals(0, parkLocation.getParkID());
        assertNull(parkLocation.getZipcode());
        assertNull(parkLocation.getNumber());
        assertNull(parkLocation.getDescription());
        assertNull(parkLocation.getName());
        assertNull(parkLocation.getParkAmenities());
    }


}
