package com.reisparadijs.reisparadijs.utilities.config;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 12 August Monday 2024 - 17:10
 */

public class PasswordEncoder {

    /**
     * Encodes a password using the BCrypt algorithm.
     *
     * @param  password the password to be encoded
     * @return          the encoded password
     */
    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Checks if a raw password matches the encoded password.
     *
     * @param  rawPassword the raw password to be checked
     * @param  hashedPassword the encoded password
     * @return               true if the raw password matches the encoded password
     */
    public boolean matches(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
