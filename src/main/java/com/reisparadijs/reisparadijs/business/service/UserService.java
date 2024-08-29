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
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 15:52
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccommodationRepository accommodationRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AccommodationRepository accommodationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.accommodationRepository = accommodationRepository;

    }


    /**
     * Changes the password of the currently authenticated user.
     *
     * @param  request   the request containing the new password
     * @throws BindException if the old password is incorrect or the new password is the same as the old one
     */
    public void changePassword( UpdatePasswordRequest request,AppUser user) throws BindException {
        checkPassword(request, user);
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    /**
     * Checks the validity of the provided password against the user's current password.
     *
     * @param request the UpdatePasswordRequest object containing the old and new passwords
     * @param user    the AppUser object representing the user whose password is being updated
     * @throws BindException if the old password is incorrect or the new password is the same as the old one
     */
    private void checkPassword(UpdatePasswordRequest request, AppUser user) throws BindException {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "oldPassword",
                    "Old password is not correct"));
        }

        if (request.oldPassword().equals(request.newPassword())) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "password",
                    "New password must be different from old password"));
        }

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }


    /**
     * Updates the profile information of the given user based on the provided request.
     *
     * @param  request   the request containing the updated profile information
     * @param  user      the user whose profile is being updated
     * @return          the updated user with the new profile information
     */
    public AppUser updateProfile(UpdateProfileRequest request, AppUser user) {
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setInfix(request.infix());
        user.setGender(request.gender());
        return userRepository.save(user);
    }

    /**
     * Uploads a profile image for the authenticated user.
     *
     * @param  file   the multipart file containing the image
     * @return        the updated user object with the uploaded profile image
     * @throws IOException if an I/O error occurs while reading the file
     */
    public AppUser uploadProfileImage(MultipartFile file, AppUser user) throws IOException {
        if(file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        userRepository.uploadProfileImage(file, user.getId());
        user.setProfileImage(file.getBytes());
        userRepository.save(user);
        return user;
    }

    /**
     * Retrieves and returns the user details based on the provided username.
     *
     * @param  userName	the username to retrieve user details for
     * @return         	the AppUser object containing the user details
     */
    public AppUser getUserDetails(String userName) {
        return userRepository.loadUserByIndetifier(userName);
    }

    /**
     * Checks the authentication header and retrieves the user details if valid.
     *
     * @param  authenticationHeader	the authentication header to validate
     * @return         	the AppUser object containing the user details if authentication is successful
     */
    public AppUser checkAuthenticationAndGetUserDetails(String authenticationHeader) {
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid authentication header");
        }
        String jwt = authenticationHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        AppUser user = getUserDetails(username);
        if (!jwtService.validateToken(jwt, user)) {
            throw new UnauthorizedException("Invalid authentication header");
        }
        return user;

    }

    /**
     * Checks the authentication header, retrieves the user details if valid, and verifies the user has the required roles.
     *
     * @param  authenticationHeader	the authentication header to validate
     * @param  roles					the required roles for the user
     * @return         				the AppUser object containing the user details if authentication is successful and user has required roles
     */
    public AppUser checkAuthenticationAndGetUserDetails(String authenticationHeader, Role.RoleEnum ...roles) {
        var user = checkAuthenticationAndGetUserDetails(authenticationHeader);
        hasRole(user, roles);
        return user;
    }


    /**
     * Checks if the provided user has at least one of the specified roles.
     *
     * @param  user    the user to check roles for
     * @param  roles   the roles to check for
     * @throws AccessDeniedException if the user does not have any of the required roles
     */
    public void hasRole(AppUser user, Role.RoleEnum ...roles) {
        for (Role.RoleEnum role : roles) {
            if (user.getRoles().stream().map(Role::getName).toList().contains(role.toString())) {
                return;
            }
        }
        throw new AccessDeniedException("User does not have required role");

    }

    /**
     * Retrieves the list of favorite accommodations for a user based on their user ID.
     *
     * @param  userId	the ID of the user for whom to retrieve favorite accommodations
     * @return         	a list of FavoriteAccommodation objects representing the user's favorite accommodations
     */
    public List<Accommodation> getUserFavorites(int userId) {
        return accommodationRepository.getFavoriteAccommodationsByUserId(userId);
    }

    /**
     * Adds or removes favorite accommodation for a user.
     *
     * @param  request  the request containing the accommodation ID
     * @param  user     the user for whom the accommodation is being added or removed as a favorite
     * @throws NotFoundException if the accommodation with the given ID is not found
     */
    public void addOrRemoveFavoriteAccommodation(CreateFavoriteRequest request, AppUser user) {
        Accommodation accommodation = accommodationRepository
                .findById(request.accommodationId())
                .orElseThrow(() -> new NotFoundException("Accommodation not found with ID: " + request.accommodationId()));
        accommodationRepository.addOrRemoveFavoriteAccommodation(accommodation, user);
    }

}
