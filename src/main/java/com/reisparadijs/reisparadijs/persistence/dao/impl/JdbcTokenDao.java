package com.reisparadijs.reisparadijs.persistence.dao.impl;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Token;
import com.reisparadijs.reisparadijs.persistence.dao.TokenDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 23:21
 */
@Repository
public class JdbcTokenDao implements TokenDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTokenDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Token save(Token token) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> insertTokenStatement(token, connection), keyHolder);
        token.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return token;
    }



    @Override
    public void update(Token token) {

    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM token WHERE id = ?;";
        jdbcTemplate.update(sql, id);

    }

    public void delete(String token) {
        String sql = "DELETE FROM token WHERE token = ?;";
        jdbcTemplate.update(sql, token);

    }

    @Override
    public Optional<Token> findById(int id) {
        String sql = "SELECT *  FROM token WHERE id = ?;";
        try {
            Token token = jdbcTemplate.queryForObject(sql, new TokenMapper(), id);
            assert token != null;
            return Optional.of(token);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<Token> findAll() {
        return List.of();
    }

    @Override
    public Optional<Token> findByToken(String token) {
        String sql = "SELECT *  FROM token WHERE token = ?;";
        try {
            Token token1 = jdbcTemplate.queryForObject(sql, new TokenMapper(), token);
            assert token1 != null;
            return Optional.of(token1);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Token> findAllByUserId(int userId) {
        String sql = "SELECT *  FROM token WHERE user_id = ?;";
        return jdbcTemplate.query(sql, new TokenMapper(), userId);
    }

    @Override
    public Optional<Token> findByTokenTypeAndUserId(Token.TokenType tokenType, int userId) {

        String sql = "SELECT *  FROM token WHERE token_type = ? AND user_id = ?;";
        try {
            Token token = jdbcTemplate.queryForObject(sql, new TokenMapper(), tokenType.getValue(), userId);
            assert token != null;
            return Optional.of(token);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    private PreparedStatement insertTokenStatement(Token token, Connection connection) throws SQLException {

        String sql = "INSERT INTO token (token, expiration_date, token_type, user_id) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, token.getToken());
        String formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(token.getExpirationDate());
        preparedStatement.setTimestamp(2, Timestamp.valueOf(formatDate));
        preparedStatement.setString(3, token.getTokenType().toString());
        preparedStatement.setInt(4, token.getUser().getId());
        return preparedStatement;
    }

    private static class TokenMapper implements RowMapper<Token> {
        @Override
        public Token mapRow(ResultSet rs, int rowNum) throws SQLException {
            Token token = new Token();
            token.setId(rs.getInt("id"));
            token.setToken(rs.getString("token"));
            token.setExpirationDate(rs.getTimestamp("expiration_date"));
            token.setTokenType(Token.TokenType.fromValue(rs.getString("token_type")));
            token.setUser(new AppUser(rs.getInt("user_id")));
            return token;
        }
    }
}
