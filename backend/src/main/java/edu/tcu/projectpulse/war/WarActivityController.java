package edu.tcu.projectpulse.war;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserService;
import edu.tcu.projectpulse.war.dto.WarActivityRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/war-activities")
public class WarActivityController {

    private final WarActivityService warActivityService;
    private final UserService userService;

    public WarActivityController(WarActivityService warActivityService, UserService userService) {
        this.warActivityService = warActivityService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public Result getActivities(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam Integer weekId) {
        User student = resolveUser(userDetails);
        List<WarActivity> activities = warActivityService.getActivities(student.getId(), weekId);
        return new Result(true, StatusCode.SUCCESS, "Activities retrieved.", activities);
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public Result add(@AuthenticationPrincipal UserDetails userDetails,
                      @RequestBody @Valid WarActivityRequest request) {
        User student = resolveUser(userDetails);
        WarActivity activity = warActivityService.add(student.getId(), request);
        return new Result(true, StatusCode.SUCCESS, "Activity added.", activity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public Result edit(@AuthenticationPrincipal UserDetails userDetails,
                       @PathVariable Integer id,
                       @RequestBody @Valid WarActivityRequest request) {
        User student = resolveUser(userDetails);
        WarActivity activity = warActivityService.edit(id, student.getId(), request);
        return new Result(true, StatusCode.SUCCESS, "Activity updated.", activity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public Result delete(@AuthenticationPrincipal UserDetails userDetails,
                         @PathVariable Integer id) {
        User student = resolveUser(userDetails);
        warActivityService.delete(id, student.getId());
        return new Result(true, StatusCode.SUCCESS, "Activity deleted.", null);
    }

    private User resolveUser(UserDetails userDetails) {
        return userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userDetails.getUsername()));
    }
}
