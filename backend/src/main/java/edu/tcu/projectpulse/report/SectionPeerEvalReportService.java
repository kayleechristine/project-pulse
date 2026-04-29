package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluation;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluationRepository;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SectionPeerEvalReportService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final PeerEvaluationRepository peerEvaluationRepository;
    private final GradeCalculator gradeCalculator;

    public SectionPeerEvalReportService(TeamRepository teamRepository,
                                        UserRepository userRepository,
                                        PeerEvaluationRepository peerEvaluationRepository,
                                        GradeCalculator gradeCalculator) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.peerEvaluationRepository = peerEvaluationRepository;
        this.gradeCalculator = gradeCalculator;
    }

    public Map<String, Object> generate(Long sectionId, Integer weekId) {
        List<Team> teams = teamRepository.findBySectionId(sectionId);
        Set<Integer> studentIds = teams.stream()
                .flatMap(team -> team.getStudentIds().stream())
                .collect(Collectors.toSet());

        Map<Integer, User> usersById = userRepository.findAllById(studentIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        List<Map<String, Object>> entries = studentIds.stream()
                .sorted()
                .map(studentId -> buildStudentEntry(studentId, weekId, usersById.get(studentId)))
                .toList();

        return Map.of(
                "sectionId", sectionId,
                "weekId", weekId,
                "entries", entries
        );
    }

    private Map<String, Object> buildStudentEntry(Integer studentId, Integer weekId, User student) {
        List<PeerEvaluation> received = peerEvaluationRepository.findByEvaluateeIdAndWeekId(studentId, weekId);
        List<PeerEvaluation> submitted = peerEvaluationRepository.findByEvaluatorIdAndWeekId(studentId, weekId);

        Map<String, Object> entry = new HashMap<>();
        entry.put("studentId", studentId);
        entry.put("student", student == null ? null : userSummary(student));
        entry.put("grade", gradeCalculator.calculate(received));
        entry.put("publicComments", comments(received, false));
        entry.put("privateComments", comments(received, true));
        entry.put("didNotSubmit", submitted.isEmpty());
        entry.put("evaluationsReceived", received.size());
        entry.put("evaluationsSubmitted", submitted.size());
        return entry;
    }

    private List<String> comments(List<PeerEvaluation> evaluations, boolean privateComments) {
        return evaluations.stream()
                .map(privateComments ? PeerEvaluation::getPrivateComments : PeerEvaluation::getPublicComments)
                .filter(comment -> comment != null && !comment.isBlank())
                .toList();
    }

    private Map<String, Object> userSummary(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("email", user.getEmail());
        data.put("firstName", user.getFirstName());
        data.put("lastName", user.getLastName());
        return data;
    }
}
