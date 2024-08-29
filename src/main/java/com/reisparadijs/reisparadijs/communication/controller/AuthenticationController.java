package com.reisparadijs.reisparadijs.communication.controller;

import com.reisparadijs.reisparadijs.business.service.AuthenticationService;
import com.reisparadijs.reisparadijs.communication.dto.request.*;
import com.reisparadijs.reisparadijs.communication.dto.response.AuthResponse;
import com.reisparadijs.reisparadijs.communication.dto.response.BasicResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 09:57
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<BasicResponse> register(
            @RequestBody @Valid final CreateUserRequest request
    ) throws BindException {
        authenticationService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BasicResponse("User created successfully. Please check your email to verify your account."));
    }

    @GetMapping("/email-verification/{token}")
    public ResponseEntity<String> emailVerification(
            @PathVariable("token") final String token
    ) {
        authenticationService.verifyEmail(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Your email has been verified.");
    }

    @PostMapping("/resend-email-verification")
    public ResponseEntity<BasicResponse> resendVerificationLink(
            @RequestBody @Valid final ResendVerificationRequest request
    ) {
        authenticationService.resendVerificationLink(request.email());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponse("Verification link has been sent to your email."));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Validated final AuthRequest request
    ) {
        // add token headers
//        return  ResponseEntity
//                .ok()
//                .header("Authorization", "Bearer " + authenticationService.authenticate(request).accessToken())
//                .body(authenticationService.authenticate(request));
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody @Validated final RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<BasicResponse> resetPassword(
            @RequestBody @Valid final PasswordRequest request
    ) {
        authenticationService.resetPassword(request.email());
        return ResponseEntity.ok(new BasicResponse("Password reset link has been sent to your email."));
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<BasicResponse> resetPassword(
            @PathVariable("token") final String token,
            @RequestBody @Valid final ResetPasswordRequest request
    ) {
        authenticationService.resetPassword(token, request);
        return ResponseEntity.ok(new BasicResponse("Password has been reset successfully."));
    }

}
