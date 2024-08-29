package com.reisparadijs.reisparadijs.communication.controller.integration;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.service.UserService;
import com.reisparadijs.reisparadijs.communication.dto.request.AuthRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.CreateFavoriteRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.UpdatePasswordRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.UpdateProfileRequest;
import com.reisparadijs.reisparadijs.communication.dto.response.AuthResponse;
import com.reisparadijs.reisparadijs.communication.dto.response.DetailedErrorResponse;
import com.reisparadijs.reisparadijs.communication.dto.response.ErrorResponse;
import com.reisparadijs.reisparadijs.communication.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 21 August Wednesday 2024 - 22:22
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    private String authToken;

    private String password = "password2";

    @BeforeEach
    void setUp() {
        AuthRequest loginRequest = new AuthRequest("guest", password,true);
        ResponseEntity<AuthResponse> loginResponse = restTemplate
                .postForEntity("/api/auth/login", loginRequest, AuthResponse.class);
        authToken = "Bearer " + Objects.requireNonNull(loginResponse.getBody()).accessToken();
    }

    @Test
    @DisplayName("Test for authenticate user details")
    void me_ReturnsUserDetails() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        System.out.println(authToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/api/account/me",
                HttpMethod.GET,
                entity,
                UserResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().email());
        assertNotNull(response.getBody().id());
    }

    @Test
    @DisplayName("Test for unauthorized access")
    void testUnauthorizedAccess() {
        HttpHeaders headers = new HttpHeaders();
        // invalid token
        headers.set("Authorization", "");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/account/me",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void changePassword_Success() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        UpdatePasswordRequest request = new UpdatePasswordRequest("password2", "newPassword");
        HttpEntity<UpdatePasswordRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/account/change-password",
                HttpMethod.PUT,
                entity,
                String.class
        );
        password = "newPassword";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", response.getBody());
    }
    @Test
    @DisplayName("Test for change password incorrect password with bad request")
    void testChangePassword_BadRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        UpdatePasswordRequest request = new UpdatePasswordRequest("password", "newPassword");
        HttpEntity<UpdatePasswordRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<DetailedErrorResponse> response = restTemplate.exchange(
                "/api/account/change-password",
                HttpMethod.PUT,
                entity,
                DetailedErrorResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation error", response.getBody().getMessage());
        assert(!response.getBody().getItems().isEmpty());
        assertTrue(response.getBody().getItems().toString().contains("Old password is not correct"));

    }

    @Test
    @DisplayName("Test for update profile")
    void updateProfile_Success() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        UpdateProfileRequest request = new UpdateProfileRequest("John", "Doe", null, AppUser.Gender.FEMALE);
        HttpEntity<UpdateProfileRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/api/account/update-profile",
                HttpMethod.PUT,
                entity,
                UserResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().firstName());
        assertEquals("Doe", response.getBody().lastName());
    }
    @Test
    @DisplayName("Test for update profile with bad request")
    void testProfileUpdate_BadRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UpdateProfileRequest invalidRequest = new UpdateProfileRequest(null, null, null, null);
        HttpEntity<UpdateProfileRequest> entity = new HttpEntity<>(invalidRequest, headers);

        ResponseEntity<DetailedErrorResponse> response =    restTemplate
                .exchange("/api/account/update-profile", HttpMethod.PUT, entity, DetailedErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation error", response.getBody().getMessage());
        assert(!response.getBody().getItems().isEmpty());
        assertTrue(response.getBody().getItems().toString().contains("Last name is mandatory"));
        assertTrue(response.getBody().getItems().toString().contains("First name is mandatory"));

    }

    @Test
    void uploadImage_Success() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ClassPathResource("furore_logo.jpeg"));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            // fixme : not working for patch
        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/api/account/upload-image",
                HttpMethod.PUT,
                requestEntity,
                UserResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().profileImage());
    }

    @Test
    @DisplayName("Test for get favorites")
    void getFavoritesAccommodations_Success() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Accommodation>> response = restTemplate.exchange(
                "/api/account/favourites",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Accommodation>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test for add or remove favorite")
    void addOrRemoveFavoriteAccommodation_Success() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        CreateFavoriteRequest request = new CreateFavoriteRequest(1);
        HttpEntity<CreateFavoriteRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/account/favourites",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Accommodation added to favorites", response.getBody());
    }

    @Test
    @DisplayName("Test for add or remove favorite not found")
    void addOrRemoveFavoriteAccommodation_NotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        CreateFavoriteRequest request = new CreateFavoriteRequest(4);
        HttpEntity<CreateFavoriteRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/account/favourites",
                HttpMethod.POST,
                entity,
                ErrorResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Accommodation not found with ID: 4", response.getBody().getMessage());

    }

}
