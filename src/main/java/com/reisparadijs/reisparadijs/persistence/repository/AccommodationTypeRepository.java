package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.AccommodationType;
import com.reisparadijs.reisparadijs.persistence.dao.AccommodationTypeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*********************************************
 * @Author Mirre Cicilia
 * @Studentennummer 500241293
 * @Project Accommodation.java
 * @Created 11 August Sunday 2024 - 21:46
 * @Korte beschrijving: De AccommodationTypeRepository maakt, leest, wijzigt en verwjdert domeinobjecten in de database.
 **********************************************/
@Repository
public class AccommodationTypeRepository {

    private final Logger logger = LoggerFactory.getLogger(AccommodationTypeRepository.class);

    private final AccommodationTypeDao accommodationTypeDao;

    public AccommodationTypeRepository(AccommodationTypeDao accommodationTypeDao) {
        super();
        this.accommodationTypeDao = accommodationTypeDao;
        logger.info("New AccommodationTypeRepository");
    }

    public void save(AccommodationType accommodationType) {
        logger.info("Saving accommodation type with ID: {}", accommodationType.getId());
        accommodationTypeDao.save(accommodationType);
    }

    public void update(AccommodationType accommodationType) {
        logger.info("Update accommodation type with ID: {}", accommodationType.getId());
        accommodationTypeDao.update(accommodationType);
    }

    public void delete(int id) {
        logger.info("Deleting accommodation type with ID: {}", id);
        accommodationTypeDao.delete(id);
    }

    public Optional<AccommodationType> findById(int id) {
        logger.info("Find accommodation type with ID: {}", id);
        return accommodationTypeDao.findById(id);
    }

    public List<AccommodationType> findAll() {
        logger.info("Find all accommodation types.");
        return accommodationTypeDao.findAll();
    }
}
