package com.reisparadijs.reisparadijs.persistence.dao.impl;

import com.reisparadijs.reisparadijs.business.domain.MessageSubject;
import com.reisparadijs.reisparadijs.persistence.dao.MessageSubjectDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 09 August Friday 2024 - 11:30
 */

@Repository
public class JdbcMessageSubjectDao implements MessageSubjectDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMessageSubjectDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MessageSubject save(MessageSubject messageSubject) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> insertMessageSubjectStatement(messageSubject, connection), keyHolder);
        messageSubject.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return messageSubject;
    }


    @Override
    public void update(MessageSubject messageSubject) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Optional<MessageSubject> findById(int id) {
        String sql = "SELECT *  FROM message_subject WHERE id = ?;";
        try {
            MessageSubject subject = jdbcTemplate.queryForObject(sql, new JdbcMessageSubjectDao.SubjectMapper(), id);
            assert subject != null;
            return Optional.of(subject);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MessageSubject> findAll() {
        String sql = "SELECT *  FROM message_subject;";
        return jdbcTemplate.query(sql, new SubjectMapper());
    }

    private PreparedStatement insertMessageSubjectStatement(MessageSubject messageSubject, Connection connection)
            throws SQLException {
        String sql = "INSERT INTO message_subject (title) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, messageSubject.getTitle());
        return statement;
    }

    private static class SubjectMapper implements RowMapper<MessageSubject> {
        @Override
        public MessageSubject mapRow(ResultSet rs, int rowNum) throws SQLException {
            Integer id = rs.getInt("id");
            String username = rs.getString("title");
            return new MessageSubject(id, username);
        }
    }

}
