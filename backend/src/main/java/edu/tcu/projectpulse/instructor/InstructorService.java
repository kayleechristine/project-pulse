package edu.tcu.projectpulse.instructor;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.invitation.InvitationService;
import edu.tcu.projectpulse.invitation.InvitationToken;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.user.UserRole;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InstructorService {

    private final UserRepository userRepository;
    private final InvitationService invitationService;
    private final TeamRepository teamRepository;

    public InstructorService(UserRepository userRepository,
                             InvitationService invitationService,
                             TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.invitationService = invitationService;
        this.teamRepository = teamRepository;
    }

    public InvitationToken invite(String email) {
        return invitationService.generateToken(email, UserRole.INSTRUCTOR);
    }

    public List<Map<String, Object>> find(String query) {
        return userRepository.searchByRole(UserRole.INSTRUCTOR, normalize(query)).stream()
                .map(this::userSummary)
                .toList();
    }

    public Map<String, Object> view(Integer id) {
        User instructor = requireInstructor(id);
        Map<String, Object> data = new HashMap<>();
        data.put("instructor", userSummary(instructor));
        data.put("teams", teamRepository.findAllByInstructorId(id).stream().map(this::teamSummary).toList());
        data.put("accountStatus", instructor.getAccountStatus());
        return data;
    }

    public User deactivate(Integer id) {
        User instructor = requireInstructor(id);
        instructor.setEnabled(false);
        instructor.setAccountStatus(User.AccountStatus.DEACTIVATED);
        return userRepository.save(instructor);
    }

    public User reactivate(Integer id) {
        User instructor = requireInstructor(id);
        instructor.setEnabled(true);
        instructor.setAccountStatus(User.AccountStatus.ACTIVE);
        return userRepository.save(instructor);
    }

    private User requireInstructor(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if (!user.hasRole(UserRole.INSTRUCTOR)) {
            throw new ResourceNotFoundException("Instructor", "userId", id);
        }
        return user;
    }

    private String normalize(String query) {
        return query == null ? "" : query.trim();
    }

    private Map<String, Object> userSummary(User user) {
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

    private Map<String, Object> teamSummary(Team team) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", team.getId());
        data.put("sectionId", team.getSectionId());
        data.put("name", team.getName());
        data.put("description", team.getDescription());
        data.put("websiteUrl", team.getWebsiteUrl());
        return data;
    }
}
