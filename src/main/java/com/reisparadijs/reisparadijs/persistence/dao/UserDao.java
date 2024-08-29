package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 07 August Wednesday 2024 - 19:50
 */

    public interface UserDao extends GenericCrudDao<AppUser> {

        Optional<AppUser> findByEmail(String email);
        Optional<AppUser> findByUsername(String username);
        Optional<AppUser> findByUsernameOrEmail(String identifier);
        void uploadProfileImage(MultipartFile file,int id) throws IOException;

}
