package com.login.user.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.login.user.domain.exceptions.EmailMessagingException;
import com.login.user.utils.ActivateUserEmailBody;

import jakarta.mail.MessagingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ActivateUserEmailBody activateUserEmailBody;

    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom("no-reply@pedroscarelli.com");

        mailSender.send(message);
    }

    public void sendSignUpEmail(String to, UUID userId) {
        try {
            sendHtmlEmail(to, "Ativação de usuário", activateUserEmailBody.of(userId.toString()));
        } catch (MessagingException e) {
            throw new EmailMessagingException("Erro ao enviar e-mail de ativação de usuário");
        }
    }
}

