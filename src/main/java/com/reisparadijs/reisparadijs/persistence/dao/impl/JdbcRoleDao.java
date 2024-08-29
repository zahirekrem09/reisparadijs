package com.reisparadijs.reisparadijs.persistence.dao.impl;

import com.reisparadijs.reisparadijs.business.domain.Role;
import com.reisparadijs.reisparadijs.persistence.dao.RoleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 07 August Wednesday 2024 - 18:43
 */

@Repository
public class JdbcRoleDao implements RoleDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcRoleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        Logger logger = LoggerFactory.getLogger(JdbcRoleDao.class);
        logger.info("JdbcRoleDao initialized");
    }

    @Override
    public Optional<Role> findByName(String name) {
        String sql = "SELECT *  FROM role WHERE name = ?;";
        try {
            Role role = jdbcTemplate.queryForObject(sql, new RoleMapper(), name);
            assert role != null;
            return Optional.of(role);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<Role> findById(Integer id) {
        String sql = "SELECT *  FROM role WHERE id = ?;";
        try {
            Role role = jdbcTemplate.queryForObject(sql, new RoleMapper(), id);
            assert role != null;
            return Optional.of(role);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    @Override
    public void saveUserRole(int userId, int roleId) {
        String sql = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, roleId);
    }

    @Override
    public List<Role> findRolesByUserId(int id) {
        String sql = "SELECT r.id, r.name FROM role r JOIN user_role ur ON r.id = ur.role_id WHERE ur.user_id = ?";
        return jdbcTemplate.query(sql, new RoleMapper(), id);
    }

    private  static  class RoleMapper implements RowMapper<Role> {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Integer id = rs.getInt("id");
            String name = rs.getString("name");
            return new Role(id, name);
        }
    }
}
