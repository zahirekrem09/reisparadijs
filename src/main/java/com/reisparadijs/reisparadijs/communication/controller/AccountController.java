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
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 12:48
 */

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@RequestHeader String authorization) throws UnauthorizedException {
        AppUser user = userService.checkAuthenticationAndGetUserDetails(authorization);
        UserResponse userDetails = UserResponse
                .fromAppUser(user);
        return ResponseEntity.ok(userDetails);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody @Valid final UpdatePasswordRequest request, @RequestHeader String authorization
    ) throws BindException {
    AppUser user = userService.checkAuthenticationAndGetUserDetails(authorization);
        userService.changePassword(request,user);
        return ResponseEntity.ok("Password changed successfully");
    }
    @PutMapping("/update-profile")
    public ResponseEntity<UserResponse> updateProfile(
            @RequestBody @Valid final UpdateProfileRequest request,@RequestHeader String authorization
    ) {
        AppUser user = userService.checkAuthenticationAndGetUserDetails(authorization);
        return ResponseEntity.ok(UserResponse.fromAppUser( userService.updateProfile(request,user)));
    }
    // fixme : not working for patch and  data type is not correct for file (MEDIUMBLOB)
    @PutMapping(value = "/upload-image", consumes = "multipart/form-data")
    public ResponseEntity<UserResponse> uploadImage(@RequestPart("file") MultipartFile file, @RequestHeader String authorization) throws IOException {
        AppUser user = userService.checkAuthenticationAndGetUserDetails(authorization);
        return ResponseEntity.ok(UserResponse.fromAppUser( userService.uploadProfileImage(file, user) ));
    }
    @GetMapping("/favourites")
    public ResponseEntity<List<Accommodation>> getFavoritesAccommodations(@RequestHeader String authorization) {
        AppUser user = userService.checkAuthenticationAndGetUserDetails(authorization, Role.RoleEnum.ROLE_GUEST);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserFavorites(user.getId()));
    }
    @PostMapping("/favourites")
    public ResponseEntity<String> addOrRemoveFavoriteAccommodation(@RequestHeader String authorization,
                                                                   @RequestBody @Valid final CreateFavoriteRequest request) {
        AppUser user = userService.checkAuthenticationAndGetUserDetails(authorization, Role.RoleEnum.ROLE_GUEST);
        userService.addOrRemoveFavoriteAccommodation(request, user);

        return ResponseEntity.status(HttpStatus.OK).body("Accommodation added to favorites");
    }


}
