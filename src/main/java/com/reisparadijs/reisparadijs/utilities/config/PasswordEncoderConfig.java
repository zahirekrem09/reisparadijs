package com.reisparadijs.reisparadijs.utilities.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 21:36
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new PasswordEncoder();
    }
}
