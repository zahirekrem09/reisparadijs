package com.reisparadijs.reisparadijs.business.domain;

import java.util.Date;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 23:12
 */

public class Token {

    private Integer id;
    private String token;
    private Date expirationDate;
    private TokenType tokenType;
    private AppUser user;


    public Token(Integer id, String token, Date expirationDate, TokenType tokenType, AppUser user) {
        this.id = id;
        this.token = token;
        this.expirationDate = expirationDate;
        this.tokenType = tokenType;
        this.user = user;
    }

    public Token() {

    }

    public String getToken() {
        return token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public enum  TokenType {
        VERIFICATION("verification"),
        PASSWORD_RESET("password_reset");
        private final String value;

        TokenType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static TokenType fromValue(String value) {
            for (TokenType tokenType : TokenType.values()) {
                if (tokenType.getValue().equals(value)) {
                    return tokenType;
                }
            }
            return null;
        }
    }
}
