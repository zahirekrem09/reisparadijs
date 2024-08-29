package com.reisparadijs.reisparadijs.communication.dto.response;

import com.reisparadijs.reisparadijs.business.domain.Message;
import com.reisparadijs.reisparadijs.business.domain.MessageSubject;

import java.util.List;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 13 August Tuesday 2024 - 14:03
 */public record MessageResponse(
        Integer id,
        String content,
        String createdAt,
        Integer parentMessageId,
        UserResponse sender,
        UserResponse receiver,
        MessageSubject messageSubject,
        List<MessageResponse> replies
//        ReservationResponse reservation
) {
     public static MessageResponse fromMessage(Message message) {
         // fixme : populate parent message
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getCreatedAt().toString(),
                message.getParentMessage() != null ? message.getParentMessage().getId() : null,
                UserResponse.fromAppUser(message.getSender()),
                UserResponse.fromAppUser(message.getReceiver()),
                message.getMessageSubject(),
                message.getReplies().stream().map(MessageResponse::fromMessage).toList()
        );
    }
}
