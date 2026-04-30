package edu.tcu.projectpulse.invitation;

import edu.tcu.projectpulse.invitation.dto.InviteRequest;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result createInvitation(@RequestBody @Valid InviteRequest request) {
        InvitationToken token = invitationService.generateToken(request.getEmail(), request.getRole(), request.getSectionId());
        return new Result(true, StatusCode.SUCCESS, "Invitation created", token.getToken());
    }

    @GetMapping("/validate")
    public Result validateToken(@RequestParam String token) {
        InvitationToken inviteToken = invitationService.validateToken(token);
        return new Result(true, StatusCode.SUCCESS, "Token is valid", inviteToken.getEmail());
    }
}
