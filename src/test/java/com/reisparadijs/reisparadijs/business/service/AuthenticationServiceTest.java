package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Role;
import com.reisparadijs.reisparadijs.business.domain.Token;
import com.reisparadijs.reisparadijs.communication.dto.request.AuthRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.CreateUserRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.RefreshTokenRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.ResetPasswordRequest;
import com.reisparadijs.reisparadijs.communication.dto.response.AuthResponse;
import com.reisparadijs.reisparadijs.persistence.repository.RoleRepository;
import com.reisparadijs.reisparadijs.persistence.repository.UserRepository;
import com.reisparadijs.reisparadijs.utilities.authorization.JwtService;
import com.reisparadijs.reisparadijs.utilities.config.PasswordEncoder;
import com.reisparadijs.reisparadijs.utilities.event.UserEmailVerificationSendEvent;
import com.reisparadijs.reisparadijs.utilities.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.BindException;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 21 August Wednesday 2024 - 12:10
 */


class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private  TokenService tokenService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private CreateUserRequest createUserRequest;

    private  Token verificationToken;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        createUserRequest = new CreateUserRequest("test@example.com",
                "password",
                Set.of(Role.RoleEnum.ROLE_HOST),
                "firstName",
                "infix",
                "lastName",
                "test@example.com",
                AppUser.Gender.MALE
        );
        verificationToken = new Token(1, "token", new Date(), Token.TokenType.VERIFICATION, createUserRequest.toAppUser());
    }
    @Test
    @DisplayName("Happy path")
    void teRegisterSuccess() throws BindException {
        AppUser user = createUserRequest.toAppUser();
        // Mocking
        when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(1, "ROLE_HOST")));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(user);
        when(tokenService.create(user, Token.TokenType.VERIFICATION)).thenReturn(new Token(1, "token", new Date(), Token.TokenType.VERIFICATION,user));
        // Call the method
        authenticationService.register(createUserRequest);

        // Verification
        verify(userRepository, times(1)).save(any());
        verify(eventPublisher, times(1)).publishEvent(any());
    }
    @Test( )
    @DisplayName("When user email already exists")
    public void testRegister_ExistingUser() throws BindException {
        // Mocking
        when(userRepository.findByUserNameOrEmail(any(String.class))).thenReturn(Optional.of(createUserRequest.toAppUser()));
        // Call the method
        // Verification
        assertThrows(BindException.class, () -> authenticationService.register(createUserRequest));

    }

    @Test
    @DisplayName("Happy path")
    void given_whenVerifyEmail_thenAssertBody() {
        AppUser user = new AppUser();
        // Given
        when(tokenService.verifyToken(verificationToken.getToken())).thenReturn(verificationToken);
        when(userRepository.findById(verificationToken.getUser().getId())).thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(tokenService).delete(verificationToken);
        // When
        authenticationService.verifyEmail(verificationToken.getToken());
        // Then
        verify(userRepository, Mockito.times(1)).save(user);
        verify(tokenService, Mockito.times(1)).delete(verificationToken);
    }

    @Test
    @DisplayName("Token not found test")
    void given_whenVerifyEmail_thenShouldThrowNotFoundException() {
        // Given
        when(tokenService.verifyToken(verificationToken.getToken())).thenThrow(NotFoundException.class);
        // When
        // Then
        assertThrows(NotFoundException.class, () ->    authenticationService.verifyEmail(verificationToken.getToken()));
    }

    @Test
    @DisplayName("Token expired test")
    void given_whenVerifyEmail_thenShouldThrowBadRequestException() {
        // Given
        when(tokenService.verifyToken(verificationToken.getToken())).thenThrow(TokenExpiredException.class);
        // When
        // Then
        assertThrows(TokenExpiredException.class, () ->    authenticationService.verifyEmail(verificationToken.getToken()));
    }

    @Test
    @DisplayName("Test for successful login with email or username")
    void given_whenLoginWithEmail_thenAssertBody() {
        // Given
        AuthRequest authenticationRequest = new AuthRequest("test@example.com", "password", true);
        AppUser userDetails = createUserRequest.toAppUser();
        // When
        when(userRepository.loadUserByIndetifier(authenticationRequest.identifier())).thenReturn(userDetails);
        when(passwordEncoder.matches(authenticationRequest.password(), userDetails.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(anyMap(), eq(userDetails))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(anyMap(), eq(userDetails))).thenReturn("refreshToken");
        AuthResponse response = authenticationService.authenticate(authenticationRequest);
        // Then
        assertNotNull(response);
        assertEquals("accessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
    }
    @Test
    @DisplayName("Test for not found error login with username")
    void given_whenLoginWithEmail_thenShouldThrowAuthenticationCredentialsNotFoundException_fromNotFound() {
        // Given
        AuthRequest authenticationRequest = new AuthRequest("test@example.com", "password", true);;
        AppUser userDetails = createUserRequest.toAppUser();
        String message = "User not found with username or email : " + authenticationRequest.identifier();

        // When
        when(userRepository.loadUserByIndetifier(authenticationRequest.identifier()))
                .thenThrow(new NotFoundException(message));

        // Then
        assertThrows(NotFoundException.class, ()->authenticationService.authenticate(authenticationRequest));
    }
    @Test
    @DisplayName("Test for bad credentials error login")
    void given_whenLogin_thenShouldAuthenticationCredentialsNotFoundException() {
        // Given
        AuthRequest authenticationRequest = new AuthRequest("test@example.com", "password", true);;
        AppUser userDetails = createUserRequest.toAppUser();
        // When
        when(userRepository.loadUserByIndetifier(anyString())).thenReturn(userDetails);
        when(passwordEncoder.matches(authenticationRequest.password(), userDetails.getPassword())).thenReturn(false);
        // Then
        assertThrows(BadCredentialsException.class, ()->authenticationService.authenticate(authenticationRequest));
    }

    @Test
    @DisplayName("Test for disabled error login")
    void given_whenLogin_thenShouldUserDisabledException() {
        // Given
        AuthRequest authenticationRequest = new AuthRequest("test@example.com", "password", true);;
        AppUser userDetails = createUserRequest.toAppUser();
        userDetails.setEnabled(false);
        // When
        when(userRepository.loadUserByIndetifier(anyString())).thenReturn(userDetails);
        when(passwordEncoder.matches(authenticationRequest.password(), userDetails.getPassword())).thenReturn(true);
        // Then
        assertThrows(UserDisabledException.class, ()->authenticationService.authenticate(authenticationRequest));
    }

    @Test
    @DisplayName("Test for successful refresh")
    void given_whenRefresh_thenAssertBody() {
        // Given
        RefreshTokenRequest request = new RefreshTokenRequest("refreshToken");
        AppUser userDetails = mock(AppUser.class);
        // When
        when(jwtService.isTokenExpired(request.refreshToken())).thenReturn(false);
        when(jwtService.extractUsername(request.refreshToken())).thenReturn("username");
        when(userRepository.loadUserByIndetifier("username")).thenReturn(userDetails);
        when(jwtService.generateAccessToken(anyMap(), eq(userDetails))).thenReturn("newAccessToken");
        when(jwtService.generateRefreshToken(anyMap(), eq(userDetails))).thenReturn("newRefreshToken");
        // When
        AuthResponse authResponse = authenticationService.refreshToken(request);
        // Then
        assertNotNull(authResponse);
        assertEquals("newAccessToken", authResponse.accessToken());
        assertEquals("newRefreshToken", authResponse.refreshToken());

    }

    @Test
    @DisplayName("Test for expired refresh")
    void given_whenRefresh_thenShouldThrowTokenExpiredException() {
        // Given
        RefreshTokenRequest request = new RefreshTokenRequest("refreshToken");
        // When
        when(jwtService.isTokenExpired(request.refreshToken())).thenReturn(true);
        // Then
        assertThrows(TokenExpiredException.class, () -> authenticationService.refreshToken(request));
    }
    @Test
    @DisplayName("Test for successful resetPassword")
    void given_whenResetPassword_thenAssertBody() {
        // Given
//        doNothing().when(authenticationService).sendEmailPasswordResetMail(user.getEmail());


        // Given
        AppUser user = mock(AppUser.class);
        Token token = mock(Token.class);
        ResetPasswordRequest request = new ResetPasswordRequest("newPassword", "newPassword");

        when(tokenService.verifyToken("validToken")).thenReturn(token);
        when(token.getExpirationDate()).thenReturn(new Date(System.currentTimeMillis() + 10000));
        when(token.getUser()).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(request.password())).thenReturn("encodedNewPassword");

        // When
        authenticationService.resetPassword("validToken", request);
        // Then
        verify(user, times(1)).setPassword("encodedNewPassword");
        verify(userRepository, times(1)).save(user);
        verify(tokenService, times(1)).delete(token);
    }

    @Test
    @DisplayName("Test for not found error resetPassword")
    void given_whenResetPassword_thenShouldThrowNotFoundException() {
        // Given
        when(tokenService.verifyToken(verificationToken.getToken())).thenThrow(NotFoundException.class);
        // When
        // Then
        assertThrows(NotFoundException.class, () ->    authenticationService.verifyEmail(verificationToken.getToken()));
    }

    @Test
    @DisplayName("Token expired test")
    void given_whenResetPassword_thenShouldThrowBadRequestException() {
        // Given
        when(tokenService.verifyToken(verificationToken.getToken())).thenThrow(TokenExpiredException.class);
        // When
        // Then
        assertThrows(TokenExpiredException.class, () ->   authenticationService.verifyEmail(verificationToken.getToken()));
    }
    @Test
    @DisplayName("Test for successful resendEmailVerificationMail")
    void given_whenResendEmailVerificationMail_thenAssertBody() {
        // Given
        AppUser user = mock(AppUser.class);
        when(userRepository.findByUserNameOrEmail("user@example.com")).thenReturn(Optional.of(user));
        when(tokenService.create(user, Token.TokenType.VERIFICATION)).thenReturn(new Token(1, "token", new Date(), Token.TokenType.VERIFICATION,user));
        when(user.isEnabled()).thenReturn(false);
        // When
        authenticationService.resendVerificationLink("user@example.com");
        // Then
        verify(eventPublisher, times(1)).publishEvent(any(UserEmailVerificationSendEvent.class));
    }
    @Test
    @DisplayName("Test for not found error UserAlreadyVerified")
    void given_whenResendEmailVerificationMail_thenAssertUserAlreadyVerified() {
        AppUser user = mock(AppUser.class);
        when(userRepository.findByUserNameOrEmail("user@example.com")).thenReturn(Optional.of(user));
        when(user.isEnabled()).thenReturn(true);

        assertThrows(UserAlreadyVerifyException.class, () -> {
            authenticationService.resendVerificationLink("user@example.com");
        });

        verify(eventPublisher, never()).publishEvent(any(UserEmailVerificationSendEvent.class));
    }

    @Test
    void testResendVerificationLink_UserNotFound() {
        when(userRepository.findByUserNameOrEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            authenticationService.resendVerificationLink("nonexistent@example.com");
        });

        assertEquals("User not found", exception.getMessage());
    }

}