package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Role;
import com.reisparadijs.reisparadijs.communication.dto.request.CreateUserRequest;
import com.reisparadijs.reisparadijs.persistence.dao.RoleDao;
import com.reisparadijs.reisparadijs.persistence.dao.UserDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 07 August Wednesday 2024 - 21:19
 */

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    // First, define an instance of the class that we are testing:
    private final UserRepository userRepository;

    // Define mocks for all dependencies for that class:
    private final UserDao userDaoMock = Mockito.mock(UserDao.class);
    private final RoleRepository roleDaoMock = Mockito.mock(RoleRepository.class);

    // Define some test data:
    private static CreateUserRequest createUserRequest = new CreateUserRequest(
            "username",
            "password",
            Set.of(Role.RoleEnum.ROLE_HOST),
            "firstName",
            "infix",
            "lastName",
            "email",
            AppUser.Gender.MALE
    );

    private static Role role = new Role(1, "ROLE_HOST");

    public UserRepositoryTest() {
        this.userRepository = new UserRepository(userDaoMock, roleDaoMock);
    }

    @BeforeAll
    public void setup() {

        when(userDaoMock.findById(1)).thenReturn(Optional.of(createUserRequest.toAppUser()));
        when(userDaoMock.findByUsernameOrEmail(Mockito.anyString())).thenReturn(Optional.of(createUserRequest.toAppUser()));
        when(roleDaoMock.findByName(Mockito.anyString())).thenReturn(Optional.of(role));
        when(userDaoMock.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Mockito.doAnswer(invocationOnMock -> {
            AppUser user = (AppUser) invocationOnMock.getArgument(0);
            user.setId(1000);
            return user;
        }).when(userDaoMock).save(createUserRequest.toAppUser());

    }


    @Test
    void saveUser_test_success() {
        AppUser user_0 = userRepository.save(createUserRequest.toAppUser());
        assertThat(user_0).isNotNull();
        assertThat(user_0.getId()).isGreaterThan(0);
        assertThat(user_0.getId()).isEqualTo(1000);

    }
//    @Test
//    void findById_test_success() {
//        AppUser expectedUser = createUserRequest.toAppUser();
//        expectedUser.setId(1);
//        // Test yapma
//        AppUser actualUser = userRepository.findById(1).get();
//        assertThat(actualUser).isNotNull();
////        assertThat(actualUser.getId()).isEqualTo(1);
//    }

    @Test
    void findByUserNameOrEmail_test_success() {
        AppUser user = userRepository.findByUserNameOrEmail("username").get();
        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(createUserRequest.toAppUser());
    }



}
