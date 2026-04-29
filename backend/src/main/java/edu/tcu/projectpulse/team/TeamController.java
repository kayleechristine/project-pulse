package edu.tcu.projectpulse.team;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class TeamController {

    private final TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
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