package com.reisparadijs.reisparadijs.utilities.event;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import org.springframework.context.ApplicationEvent;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 00:12
 */
public class UserEmailVerificationSendEvent extends ApplicationEvent {
    private final AppUser user;
    private final String token;

    public UserEmailVerificationSendEvent(Object source, AppUser user, String token) {
        super(source);
        this.user = user;
        this.token = token;
    }

    public AppUser getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
