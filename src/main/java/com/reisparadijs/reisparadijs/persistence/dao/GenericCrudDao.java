package com.reisparadijs.reisparadijs.persistence.dao;

import java.util.List;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 07 August Wednesday 2024 - 18:38
 */
public interface GenericCrudDao<T> {

    T save(T t);

    void update(T t);

    void delete(int  id);

    Optional<T> findById(int id);

    List<T> findAll();

}
