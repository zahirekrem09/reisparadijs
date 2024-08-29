package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Token;
import com.reisparadijs.reisparadijs.persistence.repository.TokenRepository;
import com.reisparadijs.reisparadijs.utilities.RandomStringGenerator;
import com.reisparadijs.reisparadijs.utilities.exceptions.NotFoundException;
import com.reisparadijs.reisparadijs.utilities.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 23:36
 */

@Service
public class TokenService {

    private static final int EMAIL_VERIFICATION_TOKEN_LENGTH = 64 ;

    @Value("${app.registration.email.token.expires-in}")
    private final Long expiresIn;

    private final TokenRepository tokenRepository;


    public TokenService(@Value("${app.registration.email.token.expires-in}") Long expiresIn, TokenRepository tokenRepository) {
        this.expiresIn = expiresIn;
        this.tokenRepository = tokenRepository;
    }


    /**
     * Is e-mail verification token expired?
     *
     * @param token Token
     * @return boolean
     */
    public boolean isEmailVerificationTokenExpired(Token token) {
        return token.getExpirationDate().before(new Date());
    }

    /**
     * Creates a new token for the given user and token type. If a token already exists for the given user and token type,
     * it updates the existing token with a new token value and expiration date.
     *
     * @param user        the user for whom the token is created
     * @param tokenType   the type of token to be created
     * @return            the created or updated token
     */
    public Token create(AppUser user, Token.TokenType tokenType) {
        String newToken = new RandomStringGenerator(EMAIL_VERIFICATION_TOKEN_LENGTH).next();
        Date expirationDate = Date.from(Instant.now().plusSeconds(expiresIn));
        Optional<Token> oldToken = tokenRepository.findByTokenTypeAndUserId(tokenType, user.getId());
        Token token;
        if (oldToken.isPresent()) {
            token = oldToken.get();
            token.setToken(newToken);
            token.setExpirationDate(expirationDate);
        } else {
            token = new Token(null, newToken, expirationDate, tokenType, user);
        }
        return tokenRepository.save(token);


    }

    /**
     * Verifies a token by checking if it exists in the repository and if it has not expired.
     *
     * @param token the token to be verified
     * @return the verified token
     */
    public Token verifyToken(String token) {
        Token verificationToken = tokenRepository
                .findByToken(token)
                .orElseThrow(() -> new NotFoundException("Token not found"));
        if (isEmailVerificationTokenExpired(verificationToken)) {
            throw new TokenExpiredException("Token expired");
        }
        return verificationToken;
    }

    public void delete(Token verificationToken) {
        tokenRepository.delete(verificationToken);
    }
}
