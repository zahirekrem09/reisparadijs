package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Role;
import com.reisparadijs.reisparadijs.persistence.dao.UserDao;
import com.reisparadijs.reisparadijs.utilities.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 07 August Wednesday 2024 - 20:11
 */

@Repository
public class UserRepository {

    private final UserDao userDao;
    private final RoleRepository roleRepository;

    public UserRepository(UserDao userDao, RoleRepository roleRepository) {
        this.userDao = userDao;
        this.roleRepository = roleRepository;

    }

    public Optional<AppUser> findByUserNameOrEmail(String identifier) {
        return  userDao.findByUsernameOrEmail(identifier);
    }
    public AppUser loadUserByIndetifier(String identifier) throws NotFoundException {
        AppUser user = findByUserNameOrEmail(identifier)
                .orElseThrow(() -> new NotFoundException("User not found with username or email : " + identifier));
        List<Role> roles = roleRepository.findRolesByUserId(user.getId());
        user.setRoles(roles);
        return user;
    }

    public AppUser save(AppUser appUser) {
        AppUser user;
        if (appUser.getId() == null) {
            // Create user
            user = userDao.save(appUser);
            // Assign roles to db
            user.getRoles().forEach(role -> roleRepository.saveUserRole(user.getId(), role.getId()));
        }
        else {
            user = appUser;
            // Update user
             userDao.update(appUser);
        }

        return user;
    }
    public List<AppUser> findAll() {
        return userDao.findAll();
    }

    public Optional<AppUser> findById(Integer id) {
        return userDao.findById(id);
    }

    // fixme : delete user and roles as well --- deleteById
    public void deleteById(int id) {
        userDao.delete(id);
    }

    public void uploadProfileImage(MultipartFile file, Integer id) throws IOException {
        userDao.uploadProfileImage(file, id);
    }

}
