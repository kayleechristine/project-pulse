package edu.tcu.projectpulse.student;

import edu.tcu.projectpulse.invitation.InvitationToken;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/invitations")
    @PreAuthorize("hasRole('ADMIN')")
    public Result invite(@RequestBody @Valid StudentInviteRequest request) {
        InvitationToken token = studentService.invite(request.email(), request.sectionId());
        return new Result(true, StatusCode.SUCCESS, "Student invitation created", token.getToken());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    public Result find(@RequestParam(required = false) String search) {
        return new Result(true, StatusCode.SUCCESS, "Students retrieved", studentService.find(search));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    public Result view(@PathVariable Integer id) {
        return new Result(true, StatusCode.SUCCESS, "Student details retrieved", studentService.view(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result delete(@PathVariable Integer id) {
        studentService.delete(id);
        return new Result(true, StatusCode.SUCCESS, "Student deleted", null);
    }

    public record StudentInviteRequest(
            @NotBlank(message = "Email is required") @Email(message = "Email must be a valid address") String email,
            Long sectionId
    ) {
    }
}
