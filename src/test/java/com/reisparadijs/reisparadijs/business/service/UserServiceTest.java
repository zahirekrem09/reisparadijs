package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Role;
import com.reisparadijs.reisparadijs.communication.dto.request.CreateFavoriteRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.UpdatePasswordRequest;
import com.reisparadijs.reisparadijs.communication.dto.request.UpdateProfileRequest;
import com.reisparadijs.reisparadijs.persistence.repository.AccommodationRepository;
import com.reisparadijs.reisparadijs.persistence.repository.UserRepository;
import com.reisparadijs.reisparadijs.utilities.authorization.JwtService;
import com.reisparadijs.reisparadijs.utilities.config.PasswordEncoder;
import com.reisparadijs.reisparadijs.utilities.exceptions.AccessDeniedException;
import com.reisparadijs.reisparadijs.utilities.exceptions.NotFoundException;
import com.reisparadijs.reisparadijs.utilities.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 21 August Wednesday 2024 - 20:30
 */

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AccommodationRepository accommodationRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Change password test")
    void changePassword_Success() throws BindException {
        UpdatePasswordRequest request = new UpdatePasswordRequest("oldPass", "newPass");
        AppUser user = new AppUser();
        user.setPassword("encodedOldPass");

        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        userService.changePassword(request, user);

        assertEquals("encodedNewPass", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Change password test with wrong old password")
    void changePassword_OldPasswordIncorrect() {
        UpdatePasswordRequest request = new UpdatePasswordRequest("wrongOldPass", "newPass");
        AppUser user = new AppUser();
        user.setPassword("encodedOldPass");

        when(passwordEncoder.matches("wrongOldPass", "encodedOldPass")).thenReturn(false);

        assertThrows(BindException.class, () -> userService.changePassword(request, user));
    }

    @Test
    @DisplayName("Change password test with same new password")
    void changePassword_NewPasswordSameAsOld() {
        UpdatePasswordRequest request = new UpdatePasswordRequest("oldPass", "oldPass");
        AppUser user = new AppUser();
        user.setPassword("encodedOldPass");

        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);

        assertThrows(BindException.class, () -> userService.changePassword(request, user));
    }

    @Test
    @DisplayName("Update profile test")
    void updateProfile_Success() {
        UpdateProfileRequest request = new UpdateProfileRequest("John", "Doe", "van", AppUser.Gender.MALE);
        AppUser user = new AppUser();

        when(userRepository.save(user)).thenReturn(user);

        AppUser updatedUser = userService.updateProfile(request, user);

        assertEquals("John", updatedUser.getFirstName());
        assertEquals("Doe", updatedUser.getLastName());
        assertEquals("van", updatedUser.getInfix());
        assertEquals("male", updatedUser.getGender());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Upload profile image test")
    void uploadProfileImage_Success() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        AppUser user = new AppUser();
        user.setId(1);

        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        AppUser updatedUser = userService.uploadProfileImage(file, user);

        verify(userRepository).uploadProfileImage(file, 1);
        assertArrayEquals(new byte[]{1, 2, 3}, updatedUser.getProfileImage());
        verify(userRepository).save(user);
    }
    @Test
    @DisplayName("Upload profile image test with empty file")
    void uploadProfileImage_EmptyFile_ThrowsIllegalArgumentException() {
        MultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);
        AppUser user = new AppUser();
        assertThrows(IllegalArgumentException.class, () -> userService.uploadProfileImage(emptyFile, user));
    }

    @Test
    @DisplayName("Get user details test")
    void getUserDetails_Success() {
        String userName = "testUser";
        AppUser expectedUser = new AppUser();
        when(userRepository.loadUserByIndetifier(userName)).thenReturn(expectedUser);

        AppUser result = userService.getUserDetails(userName);

        assertEquals(expectedUser, result);
    }

    @Test
    @DisplayName("Check authentication and get user details test")
    void checkAuthenticationAndGetUserDetails_Success() {
        String authHeader = "Bearer validToken";
        String username = "testUser";
        AppUser user = new AppUser();

        when(jwtService.extractUsername("validToken")).thenReturn(username);
        when(userRepository.loadUserByIndetifier(username)).thenReturn(user);
        when(jwtService.validateToken("validToken", user)).thenReturn(true);

        AppUser result = userService.checkAuthenticationAndGetUserDetails(authHeader);

        assertEquals(user, result);
    }

    @Test
    @DisplayName("Check authentication and get user details test with invalid header")
    void checkAuthenticationAndGetUserDetails_InvalidHeader() {
        String authHeader = "InvalidHeader";

        assertThrows(UnauthorizedException.class, () -> userService.checkAuthenticationAndGetUserDetails(authHeader));
    }

    @Test
    @DisplayName("Check authentication and get user details test with invalid token")
    void checkAuthenticationAndGetUserDetails_InvalidToken() {
        String authHeader = "Bearer invalidToken";
        String username = "testUser";
        AppUser user = new AppUser();

        when(jwtService.extractUsername("invalidToken")).thenReturn(username);
        when(userRepository.loadUserByIndetifier(username)).thenReturn(user);
        when(jwtService.validateToken("invalidToken", user)).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> userService.checkAuthenticationAndGetUserDetails(authHeader));
    }

    @Test
    @DisplayName("Check authentication and get user details test with roles")
    void checkAuthenticationAndGetUserDetails_WithRoles_Success() {
        String authHeader = "Bearer validToken";
        String username = "testUser";
        AppUser user = new AppUser();
        user.setRoles(List.of(new Role(1, Role.RoleEnum.ROLE_ADMIN.toString())));

        when(jwtService.extractUsername("validToken")).thenReturn(username);
        when(userRepository.loadUserByIndetifier(username)).thenReturn(user);
        when(jwtService.validateToken("validToken", user)).thenReturn(true);

        AppUser result = userService.checkAuthenticationAndGetUserDetails(authHeader, Role.RoleEnum.ROLE_ADMIN);

        assertEquals(user, result);
    }

    @Test
    @DisplayName("Check authentication and get user details test with roles access denied")
    void checkAuthenticationAndGetUserDetails_WithRoles_AccessDenied() {
        String authHeader = "Bearer validToken";
        String username = "testUser";
        AppUser user = new AppUser();
        user.setRoles(List.of(new Role(1, Role.RoleEnum.ROLE_ADMIN.toString())));

        when(jwtService.extractUsername("validToken")).thenReturn(username);
        when(userRepository.loadUserByIndetifier(username)).thenReturn(user);
        when(jwtService.validateToken("validToken", user)).thenReturn(true);
        assertThrows(AccessDeniedException.class, () -> userService.checkAuthenticationAndGetUserDetails(authHeader, Role.RoleEnum.ROLE_HOST));
    }

    @Test
    @DisplayName("Add or remove favorite accommodation test")
    void addOrRemoveFavoriteAccommodation_Success() {
        CreateFavoriteRequest request = new CreateFavoriteRequest(1);
        AppUser user = new AppUser();
        Accommodation accommodation = new Accommodation();

        when(accommodationRepository.findById(1)).thenReturn(Optional.of(accommodation));

        userService.addOrRemoveFavoriteAccommodation(request, user);

        verify(accommodationRepository).addOrRemoveFavoriteAccommodation(accommodation, user);
    }

    @Test
    @DisplayName("Add or remove favorite accommodation test with accommodation not found")
    void addOrRemoveFavoriteAccommodation_AccommodationNotFound() {
        CreateFavoriteRequest request = new CreateFavoriteRequest(1);
        AppUser user = new AppUser();

        when(accommodationRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.addOrRemoveFavoriteAccommodation(request, user));
    }

    @Test
    @DisplayName("Get user favorites test")
    void getUserFavorites_UserWithNoFavorites_ReturnsEmptyList() {
        int userId = 1;
        when(accommodationRepository.getFavoriteAccommodationsByUserId(userId)).thenReturn(Collections.emptyList());

        List<?> favorites = userService.getUserFavorites(userId);

        assertTrue(favorites.isEmpty());
    }

}