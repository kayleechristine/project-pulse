package edu.tcu.projectpulse.team;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class TeamController {

    private final TeamRepository teamRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public TeamController(TeamRepository teamRepository,
                          UserService userService,
                          UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/my-team")
    @PreAuthorize("hasRole('STUDENT')")
    public Result getMyTeam(@AuthenticationPrincipal UserDetails userDetails) {
        User student = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userDetails.getUsername()));

        Team team = teamRepository.findByStudentId(student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "studentId", student.getId()));

        List<Integer> teammateIds = team.getStudentIds().stream()
                .filter(id -> !id.equals(student.getId()))
                .collect(Collectors.toList());

        List<Map<String, Object>> teammates = userRepository.findAllById(teammateIds).stream()
                .map(u -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", u.getId());
                    m.put("firstName", u.getFirstName());
                    m.put("lastName", u.getLastName());
                    return m;
                })
                .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("teamId", team.getId());
        data.put("sectionId", team.getSectionId());
        data.put("teammates", teammates);

        return new Result(true, StatusCode.SUCCESS, "Team retrieved.", data);
    }

    @GetMapping
    public List<Team> getTeams() {
        return teamRepository.findAll();
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Team createTeam(@RequestBody Team team) {
        validateTeam(team);

        if (teamRepository.findByNameIgnoreCase(team.getName()).isPresent()) {
            throw new IllegalArgumentException("Team name already exists");
        }

        team.setName(team.getName().trim());
        return teamRepository.save(team);
    }

    @PutMapping("/{id}")
    public Team updateTeam(@PathVariable Long id, @RequestBody Team request) {
        validateTeam(request);

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        teamRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("Team name already exists");
            }
        });

        team.setSectionId(request.getSectionId());
        team.setName(request.getName().trim());
        team.setDescription(request.getDescription());
        team.setWebsiteUrl(request.getWebsiteUrl());

        return teamRepository.save(team);
    }

    @PostMapping("/{teamId}/students")
    public Team assignStudentsToTeam(@PathVariable Long teamId, @RequestBody StudentAssignmentRequest request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        if (request.studentIds() == null || request.studentIds().isEmpty()) {
            throw new IllegalArgumentException("At least one student is required");
        }

        team.getStudentIds().addAll(request.studentIds());
        return teamRepository.save(team);
    }

    @DeleteMapping("/{teamId}/students/{studentId}")
    public Team removeStudentFromTeam(@PathVariable Long teamId, @PathVariable Integer studentId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        team.getStudentIds().remove(studentId);
        return teamRepository.save(team);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long id) {
        if (!teamRepository.existsById(id)) {
            throw new IllegalArgumentException("Team not found");
        }

        teamRepository.deleteById(id);
    }

    private void validateTeam(Team team) {
        if (team.getSectionId() == null) {
            throw new IllegalArgumentException("Section is required");
        }

        if (team.getName() == null || team.getName().isBlank()) {
            throw new IllegalArgumentException("Team name is required");
        }
    }

    public record StudentAssignmentRequest(Set<Integer> studentIds) {}

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }
}