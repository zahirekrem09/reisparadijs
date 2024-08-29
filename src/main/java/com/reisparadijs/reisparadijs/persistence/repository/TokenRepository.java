package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.Token;
import com.reisparadijs.reisparadijs.persistence.dao.impl.JdbcTokenDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 23:32
 */
@Repository
public class TokenRepository {
    private final JdbcTokenDao jdbcTokenDao;

    public TokenRepository(JdbcTokenDao jdbcTokenDao) {
        this.jdbcTokenDao = jdbcTokenDao;
    }

    public Optional<Token> findByToken(String token) {
        return jdbcTokenDao.findByToken(token);

    }


    public Optional<Token> findByTokenTypeAndUserId(Token.TokenType tokenType, int userId) {
        return jdbcTokenDao.findByTokenTypeAndUserId(tokenType, userId);
    }

    public Token save(Token token) {
        return jdbcTokenDao.save(token);
    }

    public List<Token> findAllByUserId(int userId) {
        return jdbcTokenDao.findAllByUserId(userId);
    }
    public void delete(Token token) {
        jdbcTokenDao.delete(token.getId());
    }
}
