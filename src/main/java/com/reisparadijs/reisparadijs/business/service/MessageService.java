package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Message;
import com.reisparadijs.reisparadijs.business.domain.MessageSubject;
import com.reisparadijs.reisparadijs.communication.dto.request.MessageRequest;
import com.reisparadijs.reisparadijs.communication.dto.response.MessageResponse;
import com.reisparadijs.reisparadijs.communication.dto.response.UserResponse;
import com.reisparadijs.reisparadijs.persistence.repository.MessageRepository;
import com.reisparadijs.reisparadijs.utilities.exceptions.AccessDeniedException;
import com.reisparadijs.reisparadijs.utilities.exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 13 August Tuesday 2024 - 13:40
 */
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {

        this.messageRepository = messageRepository;
    }

    /**
     * Retrieves a list of messages associated with the given user.
     *
     * @param user the user whose messages are to be retrieved
     * @return a list of messages associated with the given user
     */
    public List<Message> getMessages(AppUser user) {
        return messageRepository.findMessagesByUserId(user);
    }

    /**
     * Sends a message based on the given request and user.
     *
     * @param  request   the message request containing the message details
     * @param  user      the user sending the message
     * @return          the saved message
     */
    public Message sendMessage(MessageRequest request, AppUser user) {
       return  messageRepository.save(MessageRequest.toMessage(request, user));
    }

    public void deleteMessage(int id,AppUser user) {

        Message message = messageRepository.findById(id).orElseThrow(() -> new NotFoundException("Message not found with ID: " + id));
        if (!message.getSender().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this message " + id);
        }
        messageRepository.delete(message.getId());

    }


    public List<MessageSubject> findAllMessageSubjects() {
        return messageRepository.getAllMessageSubjects();
    }

}




