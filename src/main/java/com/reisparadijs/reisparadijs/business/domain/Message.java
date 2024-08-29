package com.reisparadijs.reisparadijs.business.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 09 August Friday 2024 - 10:49
 */
public class Message {

    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private Message parentMessage;
    private AppUser sender;
    private AppUser receiver;
    private Reservation reservation;
    private MessageSubject messageSubject;
    private List<Message> replies;

    public Message() {
        this.replies = new ArrayList<>();
this.createdAt = LocalDateTime.now();
    }

    public Message(Integer id, String content,
                   Message parentMessage, AppUser sender, AppUser receiver,
                   Reservation reservation, MessageSubject messageSubject) {
        this.id = id;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.parentMessage = parentMessage;
        this.sender = sender;
        this.receiver = receiver;
        this.reservation = reservation;
        this.messageSubject = messageSubject;
        this.replies = new ArrayList<>();
    }

    public Message(int id) {
        this.id = id;
    }

    public List<Message> getReplies() {
        return replies;
    }

    public void setReplies(List<Message> replies) {
        this.replies = replies;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MessageSubject getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(MessageSubject messageSubject) {
        this.messageSubject = messageSubject;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public AppUser getReceiver() {
        return receiver;
    }

    public void setReceiver(AppUser receiver) {
        this.receiver = receiver;
    }

    public AppUser getSender() {
        return sender;
    }

    public void setSender(AppUser sender) {
        this.sender = sender;
    }

    public Message getParentMessage() {
        return parentMessage;
    }

    public void setParentMessage(Message parentMessage) {
        this.parentMessage = parentMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id
                +
//                ", content='" + content + '\'' +
//                ", createdAt=" + createdAt +
//                ", parentMessage=" + parentMessage +
//                ", sender=" + sender.getId() +
//                ", receiver=" + receiver.getId() +
//                ", reservation=" + reservation.getId() +
//                ", messageSubject=" + messageSubject.getTitle() +
                '}';
    }
}
