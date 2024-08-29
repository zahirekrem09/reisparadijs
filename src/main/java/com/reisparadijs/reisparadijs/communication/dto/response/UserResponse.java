package com.reisparadijs.reisparadijs.communication.dto.response;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Role;

import java.util.List;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 14:19
 */
public record UserResponse(
        String userName,
        String email,
        Integer id,
        List<Role> roles,
        String firstName,
        String infix,
        String lastName,
        byte[] profileImage

) {
    public static UserResponse fromAppUser(AppUser appUser) {
        return new UserResponse(
                appUser.getUserName(),
                appUser.getEmail(),
                appUser.getId(),
                appUser.getRoles(),
                appUser.getFirstName(),
                appUser.getInfix(),
                appUser.getLastName(),
                appUser.getProfileImage()
        );
    }

}
