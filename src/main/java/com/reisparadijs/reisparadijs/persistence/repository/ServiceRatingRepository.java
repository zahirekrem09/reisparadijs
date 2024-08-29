package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.ServiceRating;
import com.reisparadijs.reisparadijs.persistence.dao.ServiceRatingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ServiceRatingRepository {

    private final ServiceRatingDAO serviceRatingDAO;

    @Autowired
    public ServiceRatingRepository(ServiceRatingDAO serviceRatingDAO) {
        this.serviceRatingDAO = serviceRatingDAO;
    }

    public ServiceRating save(ServiceRating serviceRating) {
        return serviceRatingDAO.save(serviceRating);
    }

    public void update(ServiceRating serviceRating) {
        serviceRatingDAO.update(serviceRating);
    }

    public void delete(int id) {
        serviceRatingDAO.delete(id);
    }

    public Optional<ServiceRating> findById(int id) {
        return serviceRatingDAO.findById(id);
    }

    public List<ServiceRating> findAll() {
        return serviceRatingDAO.findAll();
    }
}
