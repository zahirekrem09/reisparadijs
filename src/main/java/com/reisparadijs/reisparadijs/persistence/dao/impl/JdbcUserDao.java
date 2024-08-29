package com.reisparadijs.reisparadijs.persistence.dao.impl;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.persistence.dao.UserDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 07 August Wednesday 2024 - 19:53
 */

@Repository
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        String sql = "SELECT *  FROM user WHERE email = ?;";
        try {
            AppUser appUser = jdbcTemplate.queryForObject(sql, new UserMapper(), email);
            assert appUser != null;
            return Optional.of(appUser);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {
        String sql = "SELECT *  FROM user WHERE user_name = ?;";
        try {
            AppUser appUser = jdbcTemplate.queryForObject(sql, new UserMapper(), username);
            assert appUser != null;
            return Optional.of(appUser);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AppUser> findByUsernameOrEmail(String identifier) {
        String sql = "SELECT *  FROM user WHERE user_name = ? OR email = ?";
        try {
            AppUser appUser = jdbcTemplate.queryForObject(sql, new UserMapper(), identifier, identifier);
            assert appUser != null;
            return Optional.of(appUser);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void uploadProfileImage(MultipartFile file, int id) throws IOException {
        String sql = "UPDATE user SET profile_image = ? WHERE id = ?;";
        jdbcTemplate.update(sql, file.getBytes(), id);

    }


    @Override
    public Optional<AppUser> findById(int id) {
        String sql = "SELECT *  FROM user WHERE id = ?;";
        try {
            AppUser appUser = jdbcTemplate.queryForObject(sql, new UserMapper(), id);
            assert appUser != null;
            return Optional.of(appUser);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public AppUser save(AppUser appUser) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> insertUserStatement(appUser, connection), keyHolder);
        appUser.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return appUser;
    }

    @Override
    public void update(AppUser appUser) {
        String sql = """
                                UPDATE user
                SET user_name = ?, password = ?, first_name = ?, infix = ?, last_name = ?, email = ?, gender = ?, enabled =?
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql,
                appUser.getUserName(),
                appUser.getPassword(),
                appUser.getFirstName(),
                appUser.getInfix(),
                appUser.getLastName(),
                appUser.getEmail(),
                appUser.getGender(),
                appUser.isEnabled(),
                appUser.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM user WHERE id = ?;";
        jdbcTemplate.update(sql, id);

    }


    @Override
    public List<AppUser> findAll() {
        String sql = "SELECT * FROM user;";
        return jdbcTemplate.query(sql, new UserMapper());
    }


    private PreparedStatement insertUserStatement(AppUser user, Connection connection) throws SQLException {
        PreparedStatement ps;
        String sql = """
                 INSERT INTO user\s
                 (user_name, password, first_name, infix, last_name, email, gender )\s
                 VALUES (?, ?, ?, ?, ?, ?, ?);
                \s""";
        ps = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getUserName());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getFirstName());
        ps.setString(4, user.getInfix());
        ps.setString(5, user.getLastName());
        ps.setString(6, user.getEmail());
        ps.setString(7, user.getGender());
        return ps;
    }

    private static class UserMapper implements RowMapper<AppUser> {
        @Override
        public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            Integer id = rs.getInt("id");
            String username = rs.getString("user_name");
            String password = rs.getString("password");
            String firstName = rs.getString("first_name");
            String infix = rs.getString("infix");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            String gender = rs.getString("gender");
            byte[] profileImage = new byte[0];
            // check if null profile image
            if (!(rs.getBlob("profile_image") == null)) {
                profileImage = rs.getBlob("profile_image")
                        .getBytes(1, (int) rs.getBlob("profile_image").length());
            }
            Date joinedAt = rs.getDate("joined_at");
            Boolean enabled = rs.getBoolean("enabled");

            AppUser.Gender genderEnum = AppUser.Gender.fromString(gender);
            return new AppUser(id, username, password, firstName, infix, lastName, email, genderEnum, profileImage, joinedAt, enabled);
        }
    }

}
