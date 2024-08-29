package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.ServiceRating;

import java.util.List;
import java.util.Optional;

public interface ServiceRatingDAO extends GenericCrudDao<ServiceRating> {

    Optional<ServiceRating> findById(int id);

    List<ServiceRating> findAll();

    ServiceRating save(ServiceRating serviceRating);

    void update(ServiceRating serviceRating);

    void delete(int id);

    List<ServiceRating> findServiceRatingByAccommdationId(int accommodationId);
}
