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
import com.reisparadijs.reisparadijs.utilities.event.UserPasswordResetSendEvent;
import com.reisparadijs.reisparadijs.utilities.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 21:37
 */

@Service
public class AuthenticationService {


    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;
    private final JwtService jwtService;

    private final TokenService tokenService;

    public AuthenticationService( UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,  ApplicationEventPublisher eventPublisher, JwtService jwtService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;

        this.eventPublisher = eventPublisher;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }


    /**
     * Registers a new user based on the provided CreateUserRequest.
     *
     * @param  request   the CreateUserRequest containing the user information
     * @throws BindException if there is a validation error with the request
     */
    public void register(CreateUserRequest request) throws BindException {
        checkExistingUser(request);
        AppUser appUser = request.toAppUser();
        // set password hash :
        appUser.setPassword(passwordEncoder.encode(request.password()));
        // Assign roles
        List<Role> roles = request.authorities()
                .stream()
                .map(authority -> roleRepository.findByName(authority.name()).orElse(null))
                .collect(Collectors.toList());

        appUser.setRoles(roles);
        // save user
        AppUser user = userRepository.save(appUser);
        log.info("User registered with email: {}, {}", user.getEmail(), user.getId());
        // publish email verification event
        emailVerificationEventPublisher(user);

    }
    /**
     * Checks if a user with the provided username or email already exists in the database.
     *
     * @param request the CreateUserRequest containing the user information to check for existence
     * @throws BindException if a user with the provided username or email already exists
     */
    private void checkExistingUser(CreateUserRequest request) throws BindException {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        userRepository
                .findByUserNameOrEmail(request.userName())
                .ifPresent(
                        user -> {
                            bindingResult
                                    .addError(new FieldError(bindingResult.getObjectName(),
                                            "userName",
                                            "User already exists"));
                        }
                );

        userRepository
                .findByUserNameOrEmail(request.email())
                .ifPresent(
                        user -> {
                            bindingResult
                                    .addError(new FieldError(bindingResult.getObjectName(),
                                            "email",
                                            "User already exists"));
                        }
                );

        if (bindingResult.hasErrors()) {
            // throw bind exception with validation errors
            throw new BindException(bindingResult);
        }
    }

    /**
     * Verifies an email address by enabling the associated user account.
     *
     * @param token	a verification token
     */
    public void verifyEmail(String token) {
        Token verificationToken = tokenService.verifyToken(token);
        AppUser user = userRepository
                .findById(verificationToken
                        .getUser()
                        .getId())
                .orElseThrow(()-> new NotFoundException("User not found "));

        user.setEnabled(true);
        userRepository.save(user);
        tokenService.delete(verificationToken);
    }

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param authenticationRequest	an object containing the user's identifier and password
     * @return         	an AuthResponse object containing the authentication result
     */
    public AuthResponse authenticate(AuthRequest authenticationRequest)  {
        final AppUser userDetails = userRepository.loadUserByIndetifier(authenticationRequest.identifier());
        checkEnabled(userDetails);
        checkPassword(authenticationRequest, userDetails);
        return getAuthResponse(userDetails);
    }




    /**
     * E-mail verification event publisher.
     *
     * @param user User
     */
    private void emailVerificationEventPublisher(AppUser user) {
        Token token = tokenService.create(user, Token.TokenType.VERIFICATION);
        eventPublisher.publishEvent(new UserEmailVerificationSendEvent(this, user, token.getToken()));
    }

    /**
     * Password reset event publisher.
     *
     * @param user User
     */
    private void passwordResetEventPublisher(AppUser user) {
        Token token = tokenService.create(user, Token.TokenType.PASSWORD_RESET);
        eventPublisher.publishEvent(new UserPasswordResetSendEvent(this, user, token.getToken()));
    }

    /**
     * Generates an authentication response containing an access token and a refresh token
     * based on the provided user details.
     *
     * @param  userDetails  the user details used to generate the tokens
     * @return               an AuthResponse object containing the access token and refresh token
     */
    private AuthResponse getAuthResponse(AppUser userDetails) {
        var claims = new HashMap<String, Object>();
        // todo : add more claims
        claims.put("id", userDetails.getId());
        final String accessToken = jwtService.generateAccessToken(claims, userDetails);
        final String refreshToken = jwtService.generateRefreshToken(claims, userDetails);
        return new AuthResponse(accessToken, refreshToken);
    }

    /**
     * Refreshes an authentication token based on the provided refresh token request.
     *
     * @param  request  the refresh token request containing the refresh token
     * @return          an AuthResponse object containing the new access token and refresh token
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        if (jwtService.isTokenExpired(refreshToken)) {
            throw new TokenExpiredException("Invalid refresh token or expired");
        }
        String username = jwtService.extractUsername(refreshToken);
        AppUser userDetails = userRepository.loadUserByIndetifier(username);
        return getAuthResponse(userDetails);
    }

    /**
     * Resets the password for the user with the given email address.
     *
     * @param  email  the email address of the user to reset the password for
     */
    public void resetPassword(String email) {
        AppUser user = userRepository
                .findByUserNameOrEmail(email)
                .orElseThrow(()-> new NotFoundException("User not found"));
        passwordResetEventPublisher(user);
    }

    /**
     * Resets the password for the user associated with the provided token and password request.
     *
     * @param  tokenString  the token string used to verify the password reset request
     * @param  request      the password reset request containing the new password
     */
    public void resetPassword(String tokenString, ResetPasswordRequest request) {

        Token resetPasswordToken = tokenService.verifyToken(tokenString);
        if (resetPasswordToken.getExpirationDate().before(new Date())) {
            throw new TokenExpiredException("Token expired or invalid");
        }
        AppUser user = userRepository
                .findById(resetPasswordToken
                        .getUser()
                        .getId())
                .orElseThrow(()-> new NotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        tokenService.delete(resetPasswordToken);
    }

    /**
     * Resends the verification link to the user with the given email address.
     *
     * @param  email  the email address of the user to resend the verification link for
     */
    public void resendVerificationLink(String email) {
        AppUser user = userRepository
                .findByUserNameOrEmail(email)
                .orElseThrow(()-> new NotFoundException("User not found"));
        if (user.isEnabled()) {
            throw new UserAlreadyVerifyException();
        }
        emailVerificationEventPublisher(user);
    }


    /**
     * Checks if the provided password matches the user's password.
     *
     * @param  authenticationRequest the authentication request containing the password
     * @param  userDetails          the user details containing the password
     * @throws BadCredentialsException if the password is incorrect
     */
    private void checkPassword(AuthRequest authenticationRequest, AppUser userDetails) {
        if (!passwordEncoder.matches(authenticationRequest.password(), userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect username - email  or password");
        }
    }

    /**
     * Checks if the given user is enabled.
     *
     * @param  userDetails   the user to check
     * @throws UserDisabledException if the user is disabled
     */
    private static void checkEnabled(AppUser userDetails) {
        if (!userDetails.isEnabled()) {
            throw new UserDisabledException("User is disabled");
        }
    }
}
