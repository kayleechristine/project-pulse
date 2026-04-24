package edu.tcu.projectpulse.auth;

import edu.tcu.projectpulse.auth.dto.LoginRequest;
import edu.tcu.projectpulse.auth.dto.LoginResponse;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.User;
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

    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
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
}
