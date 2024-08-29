package com.reisparadijs.reisparadijs.communication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 06 August Tuesday 2024 - 18:20
 */

@RestController
@RequestMapping("/api/v1/healthcheck")
public class TestController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("OK");
    }
}
