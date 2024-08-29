package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.Role;

import java.util.List;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 07 August Wednesday 2024 - 18:41
 */

public interface RoleDao {
    Optional<Role> findByName(String name);

    Optional<Role> findById(Integer id);

    void saveUserRole(int userId, int roleId) ;

    List<Role> findRolesByUserId(int id);

}
