package com.reisparadijs.reisparadijs.communication.dto.request;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Message;
import com.reisparadijs.reisparadijs.business.domain.MessageSubject;
import com.reisparadijs.reisparadijs.business.domain.Reservation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 13 August Tuesday 2024 - 14:18
 */
public record MessageRequest(
        @NotBlank
        String content,
        @NotNull

        @NotNull
        @Positive
        int receiverId,

        @NotNull
        @Positive
        int messageSubjectId,

        Integer reservationId,
        Integer parentMessageId



) {
    public static Message toMessage(MessageRequest messageRequest, AppUser user) {
        Reservation reservation = new Reservation();
        if (messageRequest.reservationId() != null && messageRequest.reservationId() > 0)
            {
                reservation.setId(messageRequest.reservationId());
            }
            return new Message(
                    null,
                    messageRequest.content(),
                    messageRequest.parentMessageId() != null ? new Message(messageRequest.parentMessageId()) : null,
                    user,
                    new AppUser(messageRequest.receiverId()),
                    messageRequest.reservationId() != null ? reservation : null,
                    new MessageSubject(messageRequest.messageSubjectId())
            );
        }
    }


