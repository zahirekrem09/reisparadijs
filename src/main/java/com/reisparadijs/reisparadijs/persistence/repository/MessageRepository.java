package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Message;
import com.reisparadijs.reisparadijs.business.domain.MessageSubject;
import com.reisparadijs.reisparadijs.persistence.dao.MessageDao;
import com.reisparadijs.reisparadijs.persistence.dao.MessageSubjectDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 09 August Friday 2024 - 14:51
 */

@Repository
public class MessageRepository {

    private final MessageDao messageDao;
    private final MessageSubjectDao messageSubjectDao;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public MessageRepository(MessageDao messageDao, MessageSubjectDao messageSubjectDao,
                             ReservationRepository reservationRepository,
                             UserRepository userRepository)
    {
        this.messageDao = messageDao;
        this.messageSubjectDao = messageSubjectDao;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    public Optional<Message> findById(Integer id) {
        Message message = messageDao.findById(id).orElse(null);
        if (message != null) {
            return Optional.of(populateMessageAssociations(message));
        }
        return Optional.empty();
    }

    public List<Message> findAllByReservationId(Integer reservationId) {
        var messages = messageDao.findAllByReservationId(reservationId);
        messages.forEach(this::populateMessageAssociations);
        return messages;
    }
 // fixme not working  sender id or receiver id
    public List<Message> findMessagesByUserId(AppUser user) {
        var messages = messageDao.findAllByUserId(user.getId());
//        return messages.stream().map(this::populateMessageAssociations).toList();
//        messages.forEach(this::populateMessageAssociations);
        messages.stream()
                .map(this::populateMessageAssociations)
                .filter(message -> message.getReplies() != null)
                .flatMap(message -> message.getReplies().stream())
                .forEach(reply -> {
                    reply.setSender(userRepository.findById(reply.getSender().getId()).orElse(null));
                    reply.setReceiver(userRepository.findById(reply.getReceiver().getId()).orElse(null));
                    reply.setMessageSubject(messageSubjectDao.findById(reply.getMessageSubject().getId()).orElse(null));
                });
        return messages.stream().filter(message -> message.getParentMessage() == null).toList();
    }

    public Message save(Message message) {
        return messageDao.save(message);
    }

    public void delete(int id) {
        messageDao.delete(id);
    }

    public List<MessageSubject> getAllMessageSubjects() {
        return messageSubjectDao.findAll();
    }

    private Message populateMessageAssociations(Message message) {
        var sender = userRepository.findById(message.getSender().getId()).orElse(null);
        var receiver = userRepository.findById(message.getReceiver().getId()).orElse(null);
        var parentMessage = messageDao.findById(message.getParentMessage().getId()).orElse(null);
        var reservation = reservationRepository.findById(message.getReservation().getId()).orElse(null);
        var messageSubject = messageSubjectDao.findById(message.getMessageSubject().getId()).orElse(null);
        var replies = messageDao.getChildMessageHierarchy(message.getId());
        message.setReservation(reservation);
        message.setParentMessage(parentMessage);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageSubject(messageSubject);
        message.setReplies(replies);
        return message;
    }



}
