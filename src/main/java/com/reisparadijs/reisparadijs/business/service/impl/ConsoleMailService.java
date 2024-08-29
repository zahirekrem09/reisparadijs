package com.reisparadijs.reisparadijs.business.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 23:06
 */

@Service
public class ConsoleMailService implements MailService {


    @JsonIgnore
    private final Logger log = LoggerFactory.getLogger(ConsoleMailService.class);


    @Override
    public void sendUserEmailVerification(AppUser user, String token) {


        log.info(String.format("[EmailService] Sending verification e-mail: %s - %s -token : %s",
                user.getId(), user.getEmail(), token));

    }

    @Override
    public void sendUserPasswordReset(AppUser user, String token) {

        log.info(String.format("[EmailService] Sending password reset e-mail: %s - %s - %s",
                user.getId(), user.getEmail(), token));
    }
}
