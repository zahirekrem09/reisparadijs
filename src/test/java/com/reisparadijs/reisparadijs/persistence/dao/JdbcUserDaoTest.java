package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.persistence.dao.impl.JdbcUserDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 07 August Wednesday 2024 - 21:18
 */

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcUserDaoTest {

    private  final UserDao instanceUnderTest;

    @Autowired
    public JdbcUserDaoTest(JdbcTemplate jdbcTemplate) {
        this.instanceUnderTest = new JdbcUserDao(jdbcTemplate);
    }


    @Test
    @DisplayName("This tests saving a NEW user in the db")
    void save_test_success() {
        // This tests saving a NEW user in the db,
        // prerequisites: 1. the user does not already exist in the db
        //                2. user.getId() == 0
        AppUser user = new AppUser(
                null,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                AppUser.Gender.MALE,
                null,
                null,
                false
        );
        user.setRoles(List.of(
        ));
        //??  stap-1: check if user is exists
        Optional<AppUser> found = instanceUnderTest.findByEmail(user.getEmail());
        assertThat(found).isNotNull().isEmpty();

        //??  stap-2: save user
        instanceUnderTest.save(user);
        //??  stap-2: save will modify user.id
        assertThat(user.getId()).isGreaterThan(0);

        //??  stap-4: make sure member is stored in the database:
        Optional<AppUser> foundAgain = instanceUnderTest.findById(user.getId());

        // ??  stap-4a: make sure at least some user is retrieved:
        assertThat(foundAgain).isNotNull().isNotEmpty();

        // ??  stap-4b: verify that this is indeed the user that we tried to store:
        assertThat(foundAgain.get()).isEqualTo(user);


    }

    @Test
    @DisplayName("This tests saving a NEW user in the db, but fails")
    void save_test_fail() {
        // This tests saving an existing user in the database;
        // prerequisites: 1. the member already exists in the db
        //                2. member.getId() > 0

        // step 1: get an existing user from the db:
        Optional<AppUser> user_option_1 = instanceUnderTest.findById(1);

        // step 2: make sure that you have found a user:
        assertThat(user_option_1).isNotNull().isNotEmpty();

        // step 3: remember its id, we will need that later:
        AppUser user_1 = user_option_1.get();
        assertThat(user_1.getId()).isGreaterThan(0);

    }

    @Test
    void find_by_non_existing_id_test() {
        Optional<AppUser> actual = instanceUnderTest.findById(10000);
        assertThat(actual).isNotNull().isEmpty();
    }

    @Test
    void find_by_existing_identifier_test() {
        Optional<AppUser> actual = instanceUnderTest.findByUsernameOrEmail("admin");
        Optional<AppUser> actual_2 = instanceUnderTest.findByUsernameOrEmail("w9bHh@example.com");
        assertThat(actual).isNotNull().isNotEmpty();
        assertThat(actual_2).isNotNull().isNotEmpty();
        assertThat(actual.get().getUserName()).isEqualTo("admin");
        assertThat(actual_2.get().getEmail()).isEqualTo("w9bHh@example.com");

        assertThat(actual.get()).isEqualTo(actual_2.get());


    }

    @Test
    void find_by_non_existing_identifier_test() {
        Optional<AppUser> actual = instanceUnderTest.findByUsernameOrEmail("notusername");
        assertThat(actual).isNotNull().isEmpty();
    }

}
