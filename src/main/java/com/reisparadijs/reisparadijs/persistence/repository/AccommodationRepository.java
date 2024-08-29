package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.persistence.dao.AccommodationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/*********************************************
 * @Author Mirre Cicilia
 * @Studentennummer 500241293
 * @Project reisparadijs
 * @Created 08 August Thursday 2024 - 13:26
 * @Korte beschrijving: De AccommodationRepository maakt, leest, wijzigt en verwjdert domeinobjecten in de database.
 **********************************************/
@Repository
public class AccommodationRepository {

    private final Logger logger = LoggerFactory.getLogger(AccommodationRepository.class);

    private final AccommodationDao accommodationDAO;

    public AccommodationRepository(AccommodationDao accommodationDAO) {
        super();
        this.accommodationDAO = accommodationDAO;
        logger.info("New AccommodationRepository");
    }

    public void save(Accommodation accommodation) {
        accommodationDAO.save(accommodation);
    }

    public void update(Accommodation accommodation) {
        accommodationDAO.update(accommodation);
    }

    public void delete(int id) {
        accommodationDAO.delete(id);
    }

    public Optional<Accommodation> findById(int id) {
        return accommodationDAO.findById(id);
    }

    public List<Accommodation> findAll() {
        return accommodationDAO.findAll();
    }

    public List<Accommodation> getFavoriteAccommodationsByUserId(int userId) {
        // fixme : getImagesByUSerId - List<Image>
        return  accommodationDAO.getFavoriteAccommodationsByUserId(userId);
    }

    public void addOrRemoveFavoriteAccommodation(Accommodation accommodation, AppUser user) {
        accommodationDAO.addOrRemoveFavoriteAccommodation(accommodation, user);

    }

    public List<Accommodation> findAccommodationsByNumberOfGuests(int numberOfGuests) {
        return accommodationDAO.findAccommodationsByNumberOfGuests(numberOfGuests);
    }


    public List<Integer> findAllAvailableAccommodationIds(Date availableFrom, Date availableUntill) {
        return accommodationDAO.findAllAvailableAccommodationIds(availableFrom, availableUntill);
    }

    public Optional<Accommodation> findByHouseNumerAndZipCode(String houseNumber, String zipCode){
        return accommodationDAO.findByHouseNumerAndZipCode(houseNumber, zipCode);
    }

}


