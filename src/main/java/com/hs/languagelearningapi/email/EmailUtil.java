package com.hs.languagelearningapi.email;

import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailUtil {
    private final EmailService emailService;
    private static final Logger log = LoggerFactory.getLogger(EmailUtil.class);

    public EmailUtil(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendEmail(String recipient, String subject, String template, Map<String, Object> properties){
        var email = Email.builder()
                .from("Event.io")
                .to(recipient)
                .subject(subject)
                .template(template)
                .properties(properties)
                .build();
        try {
            emailService.sendHtmlEmail(email);
        }
        catch (MessagingException exception){
            log.error("Could not send email because: {}", exception.getMessage());
        }
    }
}
