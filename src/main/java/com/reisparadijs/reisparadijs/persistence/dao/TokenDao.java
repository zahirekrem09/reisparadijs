package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.Token;

import java.util.List;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 23:20
 */

public interface TokenDao extends GenericCrudDao<Token> {
    Optional<Token> findByToken(String token);
    List<Token> findAllByUserId(int userId);

    Optional<Token> findByTokenTypeAndUserId(Token.TokenType tokenType, int userId);
}
