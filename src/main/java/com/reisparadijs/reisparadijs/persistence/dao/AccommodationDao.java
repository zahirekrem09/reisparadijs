package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.AppUser;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/*********************************************
 * @Author Mirre Cicilia
 * @Studentennummer 500241293
 * @Project reisparadijs
 * @Created 07 August Wednesday 2024 - 21:19
 * @Korte beschrijving: Interface voor de Data Access Object (DAO) van accommodaties.
 *  Deze interface extends de GenericCrudDao en zijn basisbewerkingen voor het beheren van
 *  accommodatiegegevens in de database.
 **********************************************/
public interface AccommodationDao extends GenericCrudDao<Accommodation> {

    // zoek naar beschikbare huisjes binnen een opgegeven periode.
    List<Accommodation> findAvailableAccommodations(LocalDateTime startDate, LocalDateTime endDate);

    // beschikbaarheidsstatus van een accommodatie bijwerken
    void updateAccommodationStatus(int id, boolean isAvailable);

    List<Accommodation> getFavoriteAccommodationsByUserId(int userId);

    void addOrRemoveFavoriteAccommodation(Accommodation accommodation, AppUser user);

    List<Accommodation> findAccommodationsByNumberOfGuests(int numberOfGuests);

    List<Integer> findAllAvailableAccommodationIds(Date availableFrom, Date availableUntill);
    Optional<Accommodation> findByHouseNumerAndZipCode(String houseNumber, String zipcode);

}
