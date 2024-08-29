package com.reisparadijs.reisparadijs.communication.dto.response;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 06 August Tuesday 2024 - 18:20
 */
public record AuthResponse(
        String accessToken,
        String refreshToken

) {
}