package edu.tcu.projectpulse.user;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.invitation.InvitationService;
import edu.tcu.projectpulse.invitation.InvitationToken;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.dto.RegisterRequest;
import edu.tcu.projectpulse.user.dto.UpdateProfileRequest;
import edu.tcu.projectpulse.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    @PostMapping("/api/auth/register")
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

    @GetMapping("/api/users/me")
    public Result getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userDetails.getUsername()));
        return new Result(true, StatusCode.SUCCESS, "Profile retrieved", new UserProfileResponse(user));
    }

    @PutMapping("/api/users/me")
    public Result updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestBody @Valid UpdateProfileRequest request) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userDetails.getUsername()));

        if (!user.getEmail().equalsIgnoreCase(request.getEmail())) {
            userService.findByEmail(request.getEmail()).ifPresent(existing -> {
                throw new ValidationException("Email is already in use.");
            });
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        User updated = userService.save(user);
        return new Result(true, StatusCode.SUCCESS, "Profile updated", new UserProfileResponse(updated));
    }
}
