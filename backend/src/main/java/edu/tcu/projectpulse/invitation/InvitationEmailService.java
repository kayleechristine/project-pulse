package edu.tcu.projectpulse.invitation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;

@Service
public class InvitationEmailService {

    private static final Logger log = LoggerFactory.getLogger(InvitationEmailService.class);

    private final JavaMailSender mailSender;
    private final String frontendBaseUrl;
    private final String fromAddress;

    public InvitationEmailService(JavaMailSender mailSender,
                                  @Value("${app.frontend-base-url:http://localhost:5173}") String frontendBaseUrl,
                                  @Value("${spring.mail.username:no-reply@project-pulse.local}") String fromAddress) {
        this.mailSender = mailSender;
        this.frontendBaseUrl = frontendBaseUrl;
        this.fromAddress = fromAddress;
    }

    public void sendInvitation(InvitationToken invitationToken) {
        String link = buildInvitationLink(invitationToken.getToken());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(invitationToken.getEmail());
        message.setSubject("Project Pulse invitation");
        message.setText("""
                You have been invited to Project Pulse.

                Use this link to create your account:
                %s

                This invitation expires in 7 days.
                """.formatted(link));

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            log.warn("Failed to send invitation email to {}: {}", invitationToken.getEmail(), ex.getMessage());
        }
    }

    public String buildInvitationLink(String token) {
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        return frontendBaseUrl.replaceAll("/+$", "") + "/register?token=" + encodedToken;
    }
}
