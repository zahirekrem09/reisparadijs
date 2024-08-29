package com.reisparadijs.reisparadijs.communication.controller;


import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Role;
import com.reisparadijs.reisparadijs.business.service.UserService;
import com.reisparadijs.reisparadijs.communication.dto.request.CreateFavoriteRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.UpdatePasswordRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.UpdateProfileRequest;
import com.reisparadijs.reisparadijs.communication.dto.response.UserResponse;
import com.reisparadijs.reisparadijs.utilities.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 21 August Wednesday 2024 - 22:08
 */
class AccountControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test for authenticate user details")
    void me_ReturnsUserResponse() throws UnauthorizedException {
        String auth = "Bearer token";
        AppUser user = new AppUser(
                1,
                "test@example.com",
                "password",
                "John",
                "Doe",
                "test",
                "test@example.com",
                AppUser.Gender.MALE,
                null,
                new Date(),
                true
        );

        user.setRoles(List.of(new Role(1, Role.RoleEnum.ROLE_GUEST.toString())));


        when(userService.checkAuthenticationAndGetUserDetails(auth)).thenReturn(user);

        ResponseEntity<UserResponse> response = accountController.me(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().id());
        assertEquals("test@example.com", response.getBody().userName());
        assertEquals("John", response.getBody().firstName());
        assertEquals("test", response.getBody().lastName());
        assertEquals("Doe", response.getBody().infix());
        assertEquals("test@example.com", response.getBody().email());


    }

    @Test
    void changePassword_Success() throws BindException {
        String auth = "Bearer token";
        UpdatePasswordRequest request = new UpdatePasswordRequest("oldPass", "newPass");
        AppUser user = new AppUser();

        when(userService.checkAuthenticationAndGetUserDetails(auth)).thenReturn(user);
        doNothing().when(userService).changePassword(request, user);

        ResponseEntity<String> response = accountController.changePassword(request, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", response.getBody());
    }

    @Test
    void updateProfile_Success() {
        String auth = "Bearer token";
        UpdateProfileRequest request = new UpdateProfileRequest("John", "Doe", null, AppUser.Gender.MALE);
        AppUser user = new AppUser();
        AppUser updatedUser = new AppUser();
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Doe");

        when(userService.checkAuthenticationAndGetUserDetails(auth)).thenReturn(user);
        when(userService.updateProfile(request, user)).thenReturn(updatedUser);

        ResponseEntity<UserResponse> response = accountController.updateProfile(request, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().firstName());
        assertEquals("Doe", response.getBody().lastName());
    }

    @Test
    void uploadImage_Success() throws IOException {
        String auth = "Bearer token";
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        AppUser user = new AppUser();
        AppUser updatedUser = new AppUser();
        updatedUser.setProfileImage("test image content".getBytes());

        when(userService.checkAuthenticationAndGetUserDetails(auth)).thenReturn(user);
        when(userService.uploadProfileImage(file, user)).thenReturn(updatedUser);

        ResponseEntity<UserResponse> response = accountController.uploadImage(file, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertArrayEquals("test image content".getBytes(), response.getBody().profileImage());
    }

    @Test
    void getFavoritesAccommodations_Success() {
        String auth = "Bearer token";
        AppUser user = new AppUser();
        user.setId(1);
        List<Accommodation> favorites = Arrays.asList(new Accommodation(), new Accommodation());

        when(userService.checkAuthenticationAndGetUserDetails(auth, Role.RoleEnum.ROLE_GUEST)).thenReturn(user);
        when(userService.getUserFavorites(1)).thenReturn(favorites);

        ResponseEntity<List<Accommodation>> response = accountController.getFavoritesAccommodations(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void addOrRemoveFavoriteAccommodation_Success() {
        String auth = "Bearer token";
        CreateFavoriteRequest request = new CreateFavoriteRequest(1);
        AppUser user = new AppUser();

        when(userService.checkAuthenticationAndGetUserDetails(auth, Role.RoleEnum.ROLE_GUEST)).thenReturn(user);
        doNothing().when(userService).addOrRemoveFavoriteAccommodation(request, user);

        ResponseEntity<String> response = accountController.addOrRemoveFavoriteAccommodation(auth, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Accommodation added to favorites", response.getBody());
    }
}
