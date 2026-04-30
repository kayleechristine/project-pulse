package edu.tcu.projectpulse.invitation;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.user.UserRole;
import edu.tcu.projectpulse.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class InvitationService {

    private static final Duration TOKEN_TTL = Duration.ofDays(7);

    private final InvitationTokenRepository tokenRepository;
    private final UserService userService;
    private final InvitationEmailService invitationEmailService;

    public InvitationService(InvitationTokenRepository tokenRepository,
                             UserService userService,
                             InvitationEmailService invitationEmailService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.invitationEmailService = invitationEmailService;
    }

    public InvitationToken generateToken(String email, UserRole role) {
        return generateToken(email, role, null);
    }

    @Transactional
    public InvitationToken generateToken(String email, UserRole role, Long sectionId) {
        if (role == UserRole.ADMIN) {
            throw new ValidationException("Cannot send invitations for the ADMIN role");
        }

        if (userService.findByEmail(email).isPresent()) {
            throw new ValidationException("A user with email " + email + " already exists");
        }

        InvitationToken token = new InvitationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setEmail(email);
        token.setRole(role);
        token.setSectionId(sectionId);
        token.setCreatedAt(Instant.now());
        token.setExpiresAt(Instant.now().plus(TOKEN_TTL));
        token.setUsed(false);

        InvitationToken savedToken = tokenRepository.save(token);
        invitationEmailService.sendInvitation(savedToken);
        return savedToken;
    }

    public InvitationToken validateToken(String rawToken) {
        InvitationToken token = tokenRepository.findByToken(rawToken)
                .orElseThrow(() -> new ResourceNotFoundException("InvitationToken", "token", rawToken));

        if (token.isUsed()) {
            throw new ValidationException("Invitation token has already been used");
        }

        if (Instant.now().isAfter(token.getExpiresAt())) {
            throw new ValidationException("Invitation token has expired");
        }

        return token;
    }

    public void markUsed(String rawToken) {
        InvitationToken token = tokenRepository.findByToken(rawToken)
                .orElseThrow(() -> new ResourceNotFoundException("InvitationToken", "token", rawToken));
        token.setUsed(true);
        tokenRepository.save(token);
    }
}
