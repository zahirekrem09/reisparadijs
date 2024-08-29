package com.reisparadijs.reisparadijs.communication.controller.integration;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Role;
import com.reisparadijs.reisparadijs.communication.dto.request.AuthRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.CreateUserRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.PasswordRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.RefreshTokenRequest;
import com.reisparadijs.reisparadijs.communication.dto.response.AuthResponse;
import com.reisparadijs.reisparadijs.communication.dto.response.BasicResponse;
import com.reisparadijs.reisparadijs.communication.dto.response.DetailedErrorResponse;
import com.reisparadijs.reisparadijs.communication.dto.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 21 August Wednesday 2024 - 20:54
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerIntegrationTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Test for register")
    void register_Success() {
        CreateUserRequest request = new CreateUserRequest("test@example.com",
                "password",
                Set.of(Role.RoleEnum.ROLE_HOST),
                "firstName",
                "lastName",
                "test@example.com",
                "infix",
                AppUser.Gender.MALE
        );

        ResponseEntity<BasicResponse> response = restTemplate.postForEntity("/api/auth/register", request, BasicResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User created successfully. Please check your email to verify your account.",
                Objects.requireNonNull(response.getBody()).message());
    }

    @Test
    @DisplayName("Test for register invalid email")
    void register_InvalidEmail() {
        CreateUserRequest request = new CreateUserRequest("test@example.com",
                "password",
                Set.of(Role.RoleEnum.ROLE_HOST),
                "firstName",
                "lastName",
                "test",
                "infix",
                AppUser.Gender.MALE
        );

        ResponseEntity<DetailedErrorResponse> response = restTemplate.postForEntity("/api/auth/register", request, DetailedErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation error", response.getBody().getMessage());
        assert (!response.getBody().getItems().isEmpty());
        assertTrue(response.getBody().getItems().toString().contains("Invalid email address"));

    }

    @Test
    @DisplayName("Test for login")
    void login_Success() {

        AuthRequest request = new AuthRequest("host", "password2", true);
        ResponseEntity<AuthResponse> response = restTemplate
                .postForEntity("/api/auth/login", request, AuthResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().accessToken());
    }

    @Test
    @DisplayName("Test for login incorrect password")
    void login_IncorrectPassword() {
        AuthRequest request = new AuthRequest("host", "password", true);
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/auth/login",
                request,
                ErrorResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Incorrect username - email  or password", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Test for login  not found user")
    void login_NotFoundUser() {

        AuthRequest request = new AuthRequest("hostts", "password2", true);
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/auth/login",
                request,
                ErrorResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found with username or email : hostts", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Test for reset password")
    void resetPassword_Success() {
        PasswordRequest request = new PasswordRequest("w9bHh1@example.com");

        ResponseEntity<BasicResponse> response = restTemplate
                .postForEntity("/api/auth/reset-password", request, BasicResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset link has been sent to your email.",
                Objects.requireNonNull(response.getBody()).message());
    }

//    @Test
//    void emailVerification_Success() {
//        String token = "validToken";
//        ResponseEntity<String> response = restTemplate
//                .getForEntity("/api/auth/email-verification/" + token, String.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Your email has been verified.", response.getBody());
//    }

    @Test
    @DisplayName("Test for email verification invalid token")
    void emailVerification_Error() {
        String token = "invalidToken";

        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/auth/email-verification/" + token, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Test for refresh token")
    void refreshToken_Success() {
        AuthRequest loginRequest = new AuthRequest("host", "password2", true);
        ResponseEntity<AuthResponse> loginResponse = restTemplate
                .postForEntity("/api/auth/login", loginRequest, AuthResponse.class);
        RefreshTokenRequest request = new RefreshTokenRequest(Objects.requireNonNull(loginResponse.getBody()).refreshToken());
        ResponseEntity<AuthResponse> response = restTemplate
                .postForEntity("/api/auth/refresh", request, AuthResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().accessToken());
        assertNotNull(response.getBody().refreshToken());
    }
}
