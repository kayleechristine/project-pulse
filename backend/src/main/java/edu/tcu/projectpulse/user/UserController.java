package edu.tcu.projectpulse.user;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.dto.UpdateProfileRequest;
import edu.tcu.projectpulse.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
