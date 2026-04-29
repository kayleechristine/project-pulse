package edu.tcu.projectpulse.instructor;

import edu.tcu.projectpulse.invitation.InvitationToken;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/instructors")
@PreAuthorize("hasRole('ADMIN')")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping("/invitations")
    public Result invite(@RequestBody @Valid InstructorInviteRequest request) {
        InvitationToken token = instructorService.invite(request.email());
        return new Result(true, StatusCode.SUCCESS, "Instructor invitation created", token.getToken());
    }

    @GetMapping
    public Result find(@RequestParam(required = false) String search) {
        return new Result(true, StatusCode.SUCCESS, "Instructors retrieved", instructorService.find(search));
    }

    @GetMapping("/{id}")
    public Result view(@PathVariable Integer id) {
        return new Result(true, StatusCode.SUCCESS, "Instructor details retrieved", instructorService.view(id));
    }

    @PatchMapping("/{id}/deactivate")
    public Result deactivate(@PathVariable Integer id) {
        return new Result(true, StatusCode.SUCCESS, "Instructor deactivated", summary(instructorService.deactivate(id)));
    }

    @PatchMapping("/{id}/reactivate")
    public Result reactivate(@PathVariable Integer id) {
        return new Result(true, StatusCode.SUCCESS, "Instructor reactivated", summary(instructorService.reactivate(id)));
    }

    private Map<String, Object> summary(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("email", user.getEmail());
        data.put("firstName", user.getFirstName());
        data.put("lastName", user.getLastName());
        data.put("roles", user.getRoles());
        data.put("accountStatus", user.getAccountStatus());
        data.put("enabled", user.isEnabled());
        return data;
    }

    public record InstructorInviteRequest(
            @NotBlank(message = "Email is required") @Email(message = "Email must be a valid address") String email
    ) {
    }
}
