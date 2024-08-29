package com.reisparadijs.reisparadijs.communication.controller;

import static org.junit.jupiter.api.Assertions.*;


import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Role;
import com.reisparadijs.reisparadijs.business.service.AuthenticationService;
import com.reisparadijs.reisparadijs.communication.dto.request.*;
import com.reisparadijs.reisparadijs.communication.dto.response.AuthResponse;
import com.reisparadijs.reisparadijs.communication.dto.response.BasicResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;

import java.util.Objects;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 21 August Wednesday 2024 - 20:52
 */

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_Success() throws BindException {
        CreateUserRequest request = new CreateUserRequest("test@example.com",
                "password",
                Set.of(Role.RoleEnum.ROLE_HOST),
                "firstName",
                "lastName",
                "test@example.com",
                "infix",
                AppUser.Gender.MALE
        );
        doNothing().when(authenticationService).register(request);

        ResponseEntity<BasicResponse> response = authenticationController.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User created successfully. Please check your email to verify your account.",
                Objects.requireNonNull(response.getBody()).message());
        verify(authenticationService).register(request);
    }

    @Test
    void emailVerification_Success() {
        String token = "validToken";
        doNothing().when(authenticationService).verifyEmail(token);

        ResponseEntity<String> response = authenticationController.emailVerification(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Your email has been verified.", response.getBody());
        verify(authenticationService).verifyEmail(token);
    }

    @Test
    void resendVerificationLink_Success() {
        ResendVerificationRequest request = new ResendVerificationRequest("test@example.com");
        doNothing().when(authenticationService).resendVerificationLink(request.email());

        ResponseEntity<BasicResponse> response = authenticationController.resendVerificationLink(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Verification link has been sent to your email.",
                Objects.requireNonNull(response.getBody()).message());
        verify(authenticationService).resendVerificationLink(request.email());
    }

    @Test
    void login_Success() {
        AuthRequest request = new AuthRequest("test@example.com", "password", true);
        AuthResponse authResponse = new AuthResponse("accessToken", "refreshToken");
        when(authenticationService.authenticate(request)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authenticationController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        verify(authenticationService).authenticate(request);
    }

    @Test
    void refreshToken_Success() {
        RefreshTokenRequest request = new RefreshTokenRequest("refreshToken");
        AuthResponse authResponse = new AuthResponse("newAccessToken", "newRefreshToken");
        when(authenticationService.refreshToken(request)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authenticationController.refreshToken(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        verify(authenticationService).refreshToken(request);
    }

    @Test
    void resetPassword_Success() {
        PasswordRequest request = new PasswordRequest("test@example.com");
        doNothing().when(authenticationService).resetPassword(request.email());

        ResponseEntity<BasicResponse> response = authenticationController.resetPassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset link has been sent to your email.",
                Objects.requireNonNull(response.getBody()).message());
        verify(authenticationService).resetPassword(request.email());
    }

    @Test
    void resetPasswordWithToken_Success() {
        String token = "resetToken";
        ResetPasswordRequest request = new ResetPasswordRequest("newPassword", "newPassword");
        doNothing().when(authenticationService).resetPassword(token, request);

        ResponseEntity<BasicResponse> response = authenticationController.resetPassword(token, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset Successfully.",
                Objects.requireNonNull(response.getBody()).message());
        verify(authenticationService).resetPassword(token, request);
    }
}