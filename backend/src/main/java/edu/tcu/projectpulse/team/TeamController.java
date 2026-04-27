package edu.tcu.projectpulse.team;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }
}