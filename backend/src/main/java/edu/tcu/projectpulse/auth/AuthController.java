package edu.tcu.projectpulse.auth;

import edu.tcu.projectpulse.auth.dto.LoginRequest;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.user.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public Result register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }
}
