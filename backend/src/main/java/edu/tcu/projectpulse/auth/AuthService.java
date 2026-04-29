package edu.tcu.projectpulse.auth;

import edu.tcu.projectpulse.auth.dto.LoginRequest;
import edu.tcu.projectpulse.auth.dto.LoginResponse;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.invitation.InvitationService;
import edu.tcu.projectpulse.invitation.InvitationToken;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.dto.RegisterRequest;
import edu.tcu.projectpulse.user.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final InvitationService invitationService;

    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       InvitationService invitationService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.invitationService = invitationService;
    }

    public Result login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (user.getAccountStatus() != User.AccountStatus.ACTIVE) {
            throw new ValidationException("Account is not active. Please complete your registration.");
        }

        UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
        String token = jwtTokenProvider.generateToken(userDetails.getUsername(), userDetails.getAuthorities());

        return new Result(true, StatusCode.SUCCESS, "Login successful", new LoginResponse(token, user));
    }

    public Result register(RegisterRequest request) {
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
