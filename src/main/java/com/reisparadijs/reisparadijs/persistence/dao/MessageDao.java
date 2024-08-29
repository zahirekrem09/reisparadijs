package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.Message;

import java.util.List;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 09 August Friday 2024 - 11:59
 */
public interface MessageDao extends GenericCrudDao<Message> {

    List<Message> findAllByReservationId(int reservationId);
    List<Message> findAllBySenderId(int senderId);
    List<Message> findAllByRecieverId(int receiverId);
    List<Message> findAllByUserId(int userId);

    List<Message> getChildMessageHierarchy(int messageId);

}
