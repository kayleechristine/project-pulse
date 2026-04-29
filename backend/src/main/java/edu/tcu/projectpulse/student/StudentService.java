package edu.tcu.projectpulse.student;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.invitation.InvitationService;
import edu.tcu.projectpulse.invitation.InvitationToken;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluation;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluationRepository;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.user.UserRole;
import edu.tcu.projectpulse.war.WarActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final InvitationService invitationService;
    private final WarActivityRepository warActivityRepository;
    private final PeerEvaluationRepository peerEvaluationRepository;
    private final TeamRepository teamRepository;

    public StudentService(UserRepository userRepository,
                          InvitationService invitationService,
                          WarActivityRepository warActivityRepository,
                          PeerEvaluationRepository peerEvaluationRepository,
                          TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.invitationService = invitationService;
        this.warActivityRepository = warActivityRepository;
        this.peerEvaluationRepository = peerEvaluationRepository;
        this.teamRepository = teamRepository;
    }

    public InvitationToken invite(String email, Long sectionId) {
        return invitationService.generateToken(email, UserRole.STUDENT, sectionId);
    }

    public List<Map<String, Object>> find(String query) {
        return userRepository.searchByRole(UserRole.STUDENT, normalize(query)).stream()
                .map(this::userSummary)
                .toList();
    }

    public Map<String, Object> view(Integer id) {
        User student = requireStudent(id);
        Map<String, Object> data = new HashMap<>();
        data.put("student", userSummary(student));
        data.put("team", teamRepository.findByStudentId(id).map(this::teamSummary).orElse(null));
        data.put("wars", warActivityRepository.findByStudentId(id));
        data.put("peerEvaluations", peerEvaluationRepository.findByEvaluatorIdOrEvaluateeId(id, id));
        return data;
    }

    @Transactional
    public void delete(Integer id) {
        User student = requireStudent(id);

        teamRepository.findAllByStudentId(id).forEach(team -> {
            team.getStudentIds().remove(id);
            teamRepository.save(team);
        });

        List<PeerEvaluation> evaluations = peerEvaluationRepository.findByEvaluatorIdOrEvaluateeId(id, id);
        peerEvaluationRepository.deleteAll(evaluations);
        warActivityRepository.deleteByStudentId(id);
        userRepository.delete(student);
    }

    private User requireStudent(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if (!user.hasRole(UserRole.STUDENT)) {
            throw new ResourceNotFoundException("Student", "userId", id);
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
