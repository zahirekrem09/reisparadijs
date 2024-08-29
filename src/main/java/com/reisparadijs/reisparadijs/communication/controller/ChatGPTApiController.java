package com.reisparadijs.reisparadijs.communication.controller;

import com.reisparadijs.reisparadijs.business.service.ChatGPTService;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 7 Augustus 2024 - 14:00
 */


@RestController
@RequestMapping("/api/chatGPT")
public class ChatGPTApiController {


    @GetMapping
    public String questionChatGPT(@RequestParam String question) throws NoSuchMessageException {
        return ChatGPTService.ChatGPTQuestion(question);
    }

    @GetMapping("chatGPTLocatie")
    public String chatGPTLocation(@RequestParam String location) throws NoSuchMessageException {
        return ChatGPTService.ChatGPTLocationRequest(location);
    }

}
