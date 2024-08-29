package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.Role;
import com.reisparadijs.reisparadijs.persistence.dao.RoleDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 06 August Tuesday 2024 - 18:21
 */

@Repository
public class RoleRepository {

    private final RoleDao roleDao;

    public RoleRepository(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Optional<Role> findByName(String name) {
        return roleDao.findByName(name);
    }

    public Optional<Role> findById(Integer id) {
        return roleDao.findById(id);
    }
    public void saveUserRole(int userId, int roleId) {
        roleDao.saveUserRole(userId, roleId);
    }

    public List<Role> findRolesByUserId(int id) {
        return roleDao.findRolesByUserId(id);
    }


}

