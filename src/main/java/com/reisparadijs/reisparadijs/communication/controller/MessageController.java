package com.reisparadijs.reisparadijs.communication.controller;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.domain.Message;
import com.reisparadijs.reisparadijs.business.domain.MessageSubject;
import com.reisparadijs.reisparadijs.business.domain.ParkLocation;
import com.reisparadijs.reisparadijs.business.service.MessageService;
import com.reisparadijs.reisparadijs.business.service.UserService;
import com.reisparadijs.reisparadijs.communication.dto.request.MessageRequest;
import com.reisparadijs.reisparadijs.communication.dto.response.MessageResponse;
import jakarta.validation.Valid;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 13 August Tuesday 2024 - 13:39
 */

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<MessageResponse>> getMessages(@RequestHeader String authorization) {
        AppUser user = userService.checkAuthenticationAndGetUserDetails(authorization);
        return ResponseEntity.ok( messageService
                .getMessages(user)
                .stream()
                .map(MessageResponse::fromMessage)
                .toList() );
    }

    @PostMapping
    public ResponseEntity<String> sendMessage(
            @RequestBody @Valid MessageRequest request, @RequestHeader String authorization
    ) {
        AppUser user = userService.checkAuthenticationAndGetUserDetails(authorization);
         messageService.sendMessage(request,user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Message sent successfully");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable int id, @RequestHeader String authorization) {
        AppUser user = userService.checkAuthenticationAndGetUserDetails(authorization);
            messageService.deleteMessage(id,user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<MessageSubject>> getAllMessageSubjects() {
        return ResponseEntity.ok(messageService.findAllMessageSubjects());
    }

}
