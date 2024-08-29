package com.reisparadijs.reisparadijs.persistence.dao.impl;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Message;
import com.reisparadijs.reisparadijs.business.domain.MessageSubject;
import com.reisparadijs.reisparadijs.business.domain.Reservation;
import com.reisparadijs.reisparadijs.persistence.dao.MessageDao;
import com.reisparadijs.reisparadijs.utilities.exceptions.ParentMessageNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 09 August Friday 2024 - 11:59
 */

@Repository
public class JdbcMessageDao implements MessageDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMessageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Message save(Message message) {
        if (message.getParentMessage() != null) {
            message.setParentMessage(findById(message.getParentMessage().getId())
                    .orElseThrow(ParentMessageNotFoundException::new));
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> insertMessageStatement(message, connection), keyHolder);
        message.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return message;
    }


    @Override
    public void update(Message message) {

    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM message WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Message> findById(int id) {

        String sql = "SELECT *  FROM message WHERE id = ?;";
        try {
            Message message = jdbcTemplate.queryForObject(sql, new MessageMapper(), id);
            assert message != null;
            return Optional.of(message);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Message> findAll() {
        String sql = "SELECT *  FROM message;";
        return jdbcTemplate.query(sql, new MessageMapper());
    }

    @Override
    public List<Message> findAllByReservationId(int reservationId) {
        String sql = "SELECT *  FROM message WHERE reservation_id = ?;";
        return jdbcTemplate.query(sql, new MessageMapper(), reservationId);
    }

    @Override
     public List<Message> findAllBySenderId(int senderId) {
        String sql = "SELECT *  FROM message WHERE sender_id = ?;";
        return jdbcTemplate.query(sql, new MessageMapper(), senderId);
    }

    @Override
    public List<Message> findAllByRecieverId(int receiverId) {
        String sql = "SELECT *  FROM message WHERE reciever_id = ?;";
        return jdbcTemplate.query(sql, new MessageMapper(), receiverId);
    }

    @Override
    public List<Message> findAllByUserId(int userId) {
        String sql = "SELECT *  FROM message WHERE sender_id = ? OR reciever_id = ? order by created_at desc;";
        return jdbcTemplate.query(sql, new MessageMapper(), userId, userId);
    }

    @Override
    public List<Message> getChildMessageHierarchy(int messageId) {
        String sql = """
        WITH RECURSIVE MessageHierarchy AS (
            SELECT
                m.id,
                m.content,
                m.created_at,
                m.parent_message_id,
                m.sender_id,
                m.reciever_id,
                m.reservation_id,
                m.message_subject_id
            FROM
                message m
            WHERE
                m.parent_message_id = ?

            UNION ALL

            SELECT
                m.id,
                m.content,
                m.created_at,
                m.parent_message_id,
                m.sender_id,
                m.reciever_id,
                m.reservation_id,
                m.message_subject_id
            FROM
                message m
                INNER JOIN MessageHierarchy mh ON m.parent_message_id = mh.id
        )

        SELECT
            id,
            content,
            created_at,
            parent_message_id,
            sender_id,
            reciever_id,
            reservation_id,
            message_subject_id
        FROM
            MessageHierarchy
        ORDER BY
            created_at desc;
        """;

        return jdbcTemplate.query(sql, new MessageMapper(), messageId);
    }



    private PreparedStatement insertMessageStatement(Message message, Connection connection) throws SQLException {
        PreparedStatement ps;
        String sql = """
                INSERT INTO message (content,  parent_message_id, sender_id, reciever_id, reservation_id,message_subject_id)
                VALUES (?, ?, ?, ?, ?, ?);
               \s""";
        ps = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, message.getContent());
        if (message.getParentMessage() == null) {
            ps.setNull(2, Types.INTEGER);
        } else {
            ps.setInt(2, message.getParentMessage().getId());
        }
        ps.setInt(3, message.getSender().getId());
        ps.setInt(4, message.getReceiver().getId());
        if (message.getReservation() == null) {
            ps.setNull(5, Types.INTEGER);
        } else {
            ps.setInt(5, message.getReservation().getId());
        }
        ps.setInt(6, message.getMessageSubject().getId());
        return ps;
    }

    private static class MessageMapper implements RowMapper<Message> {
        // fixme : dat is niet goede manier om een message object te maken.

        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            Message message = new Message();
            message.setId(rs.getInt("id"));
            message.setContent(rs.getString("content"));
            Message parentMessage = new Message();
            parentMessage.setId(rs.getInt("parent_message_id"));
            message.setParentMessage(parentMessage);
            AppUser sender = new AppUser();
            sender.setId(rs.getInt("sender_id"));
            message.setSender(sender);
            AppUser receiver = new AppUser();
            receiver.setId(rs.getInt("reciever_id"));
            message.setReceiver(receiver);
            Reservation reservation = new Reservation();
            reservation.setId(rs.getInt("reservation_id"));
            message.setReservation(reservation);
            MessageSubject messageSubject = new MessageSubject();
            messageSubject.setId(rs.getInt("message_subject_id"));
            message.setMessageSubject(messageSubject);
            message.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return message;
        }
    }
}
