package edu.tcu.projectpulse.instructor;

import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.user.UserRole;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class InstructorAssignmentService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public InstructorAssignmentService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public Team assign(Long teamId, Set<Integer> instructorIds) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ValidationException("Team not found"));

        if (instructorIds == null || instructorIds.isEmpty()) {
            throw new ValidationException("At least one instructor is required");
        }

        instructorIds.forEach(this::validateInstructor);
        instructorIds.forEach(instructorId -> moveInstructorToTeam(instructorId, team));
        team.getInstructorIds().addAll(instructorIds);
        return teamRepository.save(team);
    }

    public Team remove(Long teamId, Integer instructorId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ValidationException("Team not found"));

        team.getInstructorIds().remove(instructorId);
        return teamRepository.save(team);
    }

    private void validateInstructor(Integer instructorId) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new ValidationException("Instructor not found"));

        if (!instructor.hasRole(UserRole.INSTRUCTOR)) {
            throw new ValidationException("User is not an instructor");
        }
    }

    private void moveInstructorToTeam(Integer instructorId, Team destinationTeam) {
        teamRepository.findAllByInstructorId(instructorId).stream()
                .filter(existingTeam -> !existingTeam.getId().equals(destinationTeam.getId()))
                .forEach(existingTeam -> {
                    existingTeam.getInstructorIds().remove(instructorId);
                    teamRepository.save(existingTeam);
                });
    }
}
