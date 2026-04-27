package edu.tcu.projectpulse.user;

import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.invitation.InvitationService;
import edu.tcu.projectpulse.invitation.InvitationToken;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final InvitationService invitationService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
                          InvitationService invitationService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.invitationService = invitationService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public Result register(@RequestBody @Valid RegisterRequest request) {
        InvitationToken inviteToken = invitationService.validateToken(request.getToken());

        String email = inviteToken.getEmail();

        userService.findByEmail(email).ifPresent(existing -> {
            if (existing.getAccountStatus() == User.AccountStatus.ACTIVE) {
                throw new ValidationException("An account with this email already exists. Please log in.");
            }
        });

        User user = new User();
        user.setEmail(email);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.addRole(inviteToken.getRole());
        user.setEnabled(true);
        user.setAccountStatus(User.AccountStatus.ACTIVE);

        userService.save(user);
        invitationService.markUsed(request.getToken());

        return new Result(true, StatusCode.SUCCESS, "Account created successfully. Please log in.", null);
    }
}
