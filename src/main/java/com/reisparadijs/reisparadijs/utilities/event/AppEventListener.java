package com.reisparadijs.reisparadijs.utilities.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reisparadijs.reisparadijs.business.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 23:01
 */

@Component
public class AppEventListener  {

    private final MailService mailSenderService;



    @JsonIgnore
    private final Logger log = LoggerFactory.getLogger(AppEventListener.class);

    public AppEventListener(MailService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @EventListener(UserEmailVerificationSendEvent.class)
    public void onUserEmailVerificationSendEvent(UserEmailVerificationSendEvent event) {
        log.info("[User e-mail verification mail send event listener] {} - {}",
                event.getUser().getEmail(), event.getUser().getId());
        mailSenderService.sendUserEmailVerification(event.getUser(),event.getToken());
    }

    @EventListener(UserPasswordResetSendEvent.class)
    public void onUserPasswordResetSendEvent(UserPasswordResetSendEvent event) {
        log.info("[User password reset mail send event listener] {} - {}",
                event.getUser().getEmail(), event.getUser().getId());
        mailSenderService.sendUserPasswordReset(event.getUser(),event.getToken());
    }
}
