package com.reisparadijs.reisparadijs.business.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.business.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.thymeleaf.context.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 18:56
 */
@Service
@Primary
public class GmailMailService implements MailService {

    private static final String NAME = "name";

    private static final String LAST_NAME = "lastName";

    private static final String URL = "url";

    private static final String FULL_NAME = "fullName";

    @Value("${spring.application.name}")
    private String appName;
    @Value("${app.url}")
    private String appUrl;
    @Value("${app.frontend-url}")
    private String frontendUrl;
    @Value("${spring.mail.username}")
    private String senderAddress;

    @JsonIgnore
    private final Logger log = LoggerFactory.getLogger(GmailMailService.class);

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    public GmailMailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Sends a verification email to the user's email address with a verification token.
     *
     * @param user     the user object containing the user's information
     * @param token    the verification token
     */
    @Override
    public void sendUserEmailVerification(AppUser user, String token) {
        String backendUrl = appUrl + "/api";

        log.info(String.format("[EmailService] Sending verification e-mail: %s - %s -token : %s",
                user.getId(), user.getEmail(), token));
        String url = String.format("%s/auth/email-verification/%s", backendUrl,
                token);
        final Context ctx = createContext();
        ctx.setVariable(NAME, user.getFirstName());
        ctx.setVariable(LAST_NAME, user.getLastName());
        ctx.setVariable(FULL_NAME, user.getFullName());
        ctx.setVariable(URL, url);

        String subject = "Reisparadijs - Email Verification";

        try {
            send(new InternetAddress(senderAddress, appName), new InternetAddress(user.getEmail(), user.getFirstName()),
                    subject, templateEngine.process("mail/user-email-verification", ctx));
            log.info(String.format("[EmailService] Sent verification e-mail: %s - %s",
                    user.getId(), user.getEmail()));

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error(String.format("[EmailService] Failed to send verification e-mail: %s", e.getMessage()));
        }

    }

    /**
     * Sends a password reset email to the user's email address with a password reset token.
     *
     * @param user     the user object containing the user's information
     * @param token    the password reset token
     */
    @Override
    public void sendUserPasswordReset(AppUser user, String token) {
        log.info(String.format("[EmailService] Sending password reset e-mail: %s - %s - %s",
                user.getId(), user.getEmail(), token));

        String url = String.format("%s/auth/reset-password?token=%s", frontendUrl,
               token);

        final Context ctx = createContext();
        ctx.setVariable(NAME, user.getFirstName());
        ctx.setVariable(LAST_NAME, user.getLastName());
        ctx.setVariable(FULL_NAME, user.getFullName());
        ctx.setVariable(URL, url);
        String subject = "Reisparadijs - Password Reset";
        try {
            send(new InternetAddress(senderAddress, appName), new InternetAddress(user.getEmail(), user.getFirstName()),
                    subject, templateEngine.process("mail/user-reset-password", ctx));
            log.info(String.format("[EmailService] Sent password reset e-mail: %s - %s",
                    user.getId(), user.getEmail()));
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error(String.format("[EmailService] Failed to send password reset e-mail: %s", e.getMessage()));
        }

    }

    /**
     * Create context for template engine.
     *
     * @return Context
     */
    private Context createContext() {
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("SENDER_ADDRESS", senderAddress);
        ctx.setVariable("APP_NAME", appName);
        ctx.setVariable("APP_URL", appUrl);
        ctx.setVariable("FRONTEND_URL", frontendUrl);

        return ctx;
    }

    /**
     * Send an e-mail to the specified address.
     *
     * @param from    Address who sent
     * @param to      Address who receive
     * @param subject String subject
     * @param text    String message
     * @throws MessagingException when sending fails
     */
    private void send(InternetAddress from,
                      InternetAddress to,
                      String subject,
                      String text) throws MessagingException, MailException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true);

        mailSender.send(mimeMessage);
    }
}
