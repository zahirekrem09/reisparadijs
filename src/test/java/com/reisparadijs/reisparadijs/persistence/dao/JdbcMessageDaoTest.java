package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Message;
import com.reisparadijs.reisparadijs.business.domain.MessageSubject;
import com.reisparadijs.reisparadijs.persistence.dao.impl.JdbcMessageDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 09 August Friday 2024 - 21:35
 */

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcMessageDaoTest {

    private  final AppUser sender = new AppUser(
            3,
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
    private  final AppUser reciever = new AppUser(
            2,
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

    private  final MessageSubject messageSubject = new MessageSubject(
            1,
            "test"
    );

    private final MessageDao instanceUnderTest;

    @Autowired
    public JdbcMessageDaoTest(JdbcTemplate jdbcTemplate) {
        this.instanceUnderTest = new JdbcMessageDao(jdbcTemplate);
    }

    @Test
    @DisplayName("This tests saving a NEW message in the db")
    void save_test_success() {
        // This tests saving a NEW message in the db,


        Message message = new Message(
                null,
                "test content",
                null,
                sender,
                reciever,
                null,
                messageSubject
        );

        // ? stap-1 save message
        instanceUnderTest.save(message);
        // ? stap-2 save will modify message.id
        assertThat(message.getId()).isGreaterThan(0);
        // ? stap-3 make sure member is stored in the database:
        Optional<Message> foundAgain = instanceUnderTest.findById(message.getId());

        // ? stap-3a make sure at least some message is retrieved:
        assertThat(foundAgain).isNotNull().isNotEmpty();
        // ? stap-3b verify that this is indeed the message that we tried to store:
        assertThat(foundAgain.get().getContent()).isEqualTo(message.getContent());

    }

    @Test
    @DisplayName("This tests finding a message in the db")
    void findById_test_success() {
        // This tests finding a message in the db,

        Optional<Message> foundAgain = instanceUnderTest.findById(1);
        assertThat(foundAgain).isNotNull().isNotEmpty();
        assertThat(foundAgain.get().getSender().getId()).isEqualTo(2);
        assertThat(foundAgain.get().getReceiver().getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("This tests finding  messages in the db by sender id")
    void findAllBySenderId_test() {
        // This tests finding  messages in the db,
        List<Message> foundAgain = instanceUnderTest.findAllBySenderId(1);
        List<Message> foundAgain_2 = instanceUnderTest.findAllBySenderId(2);
        assertThat(foundAgain).isEmpty();
        assertThat(foundAgain_2).isNotNull().isNotEmpty();
        assertThat(foundAgain_2.size()).isEqualTo(2);

    }

}
