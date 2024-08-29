package com.reisparadijs.reisparadijs.business.service;

import com.reisparadijs.reisparadijs.business.domain.AppUser;
import org.springframework.stereotype.Service;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 10 August Saturday 2024 - 23:04
 */


public interface MailService {

    void sendUserEmailVerification(AppUser user, String token);

    void sendUserPasswordReset(AppUser user, String token);

}
