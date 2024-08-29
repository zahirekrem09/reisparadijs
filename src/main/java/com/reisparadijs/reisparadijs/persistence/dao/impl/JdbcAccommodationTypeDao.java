package com.reisparadijs.reisparadijs.persistence.dao.impl;

import com.reisparadijs.reisparadijs.business.domain.AccommodationType;
import com.reisparadijs.reisparadijs.persistence.dao.AccommodationTypeDao;
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

/*********************************************
 * @Author Mirre Cicilia
 * @Studentennummer 500241293
 * @Project Accommodation.java
 * @Created 11 August Sunday 2024 - 22:14
 * @Korte beschrijving: Implementatie van de Data Access Object (DAO) voor accommodaties met gebruik van JDBC.
 *  Deze klasse biedt concrete implementaties voor de methoden gedefinieerd in de
 *  AccommodationTypeDao interface, met gebruik van JDBC voor database-interacties.
 **********************************************/
@Repository
public class JdbcAccommodationTypeDao implements AccommodationTypeDao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcAccommodationDao.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccommodationTypeDao(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New JdbcMemberDao.");
    }

    @Override
    public AccommodationType save(AccommodationType accommodationType) {
        String sql = "INSERT INTO accommodation_type (name) " +
                "VALUES (?)";

        jdbcTemplate.update(sql,
                accommodationType.getName()
        );

        // haal de gegenereerde id op en sla deze op in de accommodatie
        Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        if (id != null) {
            accommodationType.setId(id);
        } else {
            throw new RuntimeException("Failed to retrieve the generated ID for the new accommodation.");
        }
        return accommodationType;
    }

    @Override
    public void update(AccommodationType accommodationType) {
        String sql = "UPDATE accommodation_type SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, accommodationType.getName(),
                accommodationType.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM accommodation_type WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<AccommodationType> findById(int id) {
        String sql = "SELECT * FROM accommodation_type WHERE id = ?";
        try {
            AccommodationType accommodationType = jdbcTemplate.queryForObject(sql, new Object[]{id},
                    new JdbcAccommodationTypeDao.AccommodationTypeRowMapper());
            return Optional.ofNullable(accommodationType);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<AccommodationType> findAll() {
        String sql = "SELECT * FROM accommodation_type";
        return jdbcTemplate.query(sql, new JdbcAccommodationTypeDao.AccommodationTypeRowMapper());
    }

    private static final class AccommodationTypeRowMapper implements RowMapper<AccommodationType> {

        @Override
        public AccommodationType mapRow(ResultSet rs, int rowNum) throws SQLException {
            AccommodationType accommodationType = new AccommodationType();
            accommodationType.setId(rs.getInt("id"));
            accommodationType.setName(rs.getString("name"));

            return accommodationType;
        }
    }
}
