package edu.tcu.projectpulse.invitation;

import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class InvitationEmailServiceTest {

    @Test
    void should_BuildInvitationLinkUsingConfiguredFrontendBaseUrl() {
        InvitationEmailService service = new InvitationEmailService(
                mock(JavaMailSender.class),
                "https://project-pulse.example.com/",
                "no-reply@example.com"
        );

        String link = service.buildInvitationLink("abc 123");

        assertThat(link).isEqualTo("https://project-pulse.example.com/register?token=abc+123");
    }
}
