package com.reisparadijs.reisparadijs.communication.dto.request;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 06 August Tuesday 2024 - 18:20
 */
public record CreateUserRequest(

        @NotEmpty(message = "Username is mandatory")
        @NotNull(message = "Username is mandatory")
        @Size(min = 4, message = "Username should be 4 characters long minimum")
        String userName,

        @NotEmpty(message = "Password is mandatory")
        @NotNull(message = "Password is mandatory")
        @Size(min = 8, message = "Password should be 8 characters long minimum")
        String password,

        //TODO : add gender enum gender enum : male, female , undisclosed

        // fixme : role enum : host, admin, guest for security
        Set<Role.RoleEnum> authorities,
//        Set<String> authorities,

        @NotEmpty(message = "First name is mandatory")
        @NotNull(message = "First name is mandatory")
        @Size(min = 3, message = "First name should be 3 characters long minimum")
        String firstName,

        @NotEmpty(message = "Last name is mandatory")
        @NotNull(message = "Last name is mandatory")
        @Size(min = 3, message = "Last name should be 3 characters long minimum")
        String lastName,

        @NotEmpty(message = "Email is mandatory")
        @NotNull(message = "Email is mandatory")
        @Email(message = "Invalid email address")
        String email,
        String infix,

        AppUser.Gender gender
) {
        public AppUser  toAppUser() {
            return new AppUser(
                    null,
                    userName,
                    password,
                    firstName,
                    infix,
                    lastName,
                    email,
                    gender,
                    null,
                    null,
                    true
            );
        }
}

