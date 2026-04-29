package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.activeweek.ActiveWeek;
import edu.tcu.projectpulse.activeweek.ActiveWeekRepository;
import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluation;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluationRepository;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentPeerEvalReportService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ActiveWeekRepository activeWeekRepository;
    private final PeerEvaluationRepository peerEvaluationRepository;
    private final GradeCalculator gradeCalculator;

    public StudentPeerEvalReportService(UserRepository userRepository,
                                        TeamRepository teamRepository,
                                        ActiveWeekRepository activeWeekRepository,
                                        PeerEvaluationRepository peerEvaluationRepository,
                                        GradeCalculator gradeCalculator) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.activeWeekRepository = activeWeekRepository;
        this.peerEvaluationRepository = peerEvaluationRepository;
        this.gradeCalculator = gradeCalculator;
    }

    public Map<String, Object> generate(Integer studentId, LocalDate startDate, LocalDate endDate) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", studentId));
        Team team = teamRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "studentId", studentId));

        List<Map<String, Object>> weeks = activeWeekRepository
                .findBySectionIdAndEndDateGreaterThanEqualAndStartDateLessThanEqualOrderByStartDateAsc(
                        team.getSectionId(), startDate, endDate)
                .stream()
                .map(week -> buildWeekEntry(studentId, week))
                .toList();

        Map<String, Object> report = new HashMap<>();
        report.put("student", userSummary(student));
        report.put("teamId", team.getId());
        report.put("sectionId", team.getSectionId());
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("weeks", weeks);
        return report;
    }

    private Map<String, Object> buildWeekEntry(Integer studentId, ActiveWeek week) {
        List<PeerEvaluation> received = peerEvaluationRepository.findByEvaluateeIdAndWeekId(studentId, week.getId().intValue());
        Map<String, Object> entry = new HashMap<>();
        entry.put("weekId", week.getId());
        entry.put("startDate", week.getStartDate());
        entry.put("endDate", week.getEndDate());
        entry.put("grade", gradeCalculator.calculate(received));
        entry.put("publicComments", comments(received, false));
        entry.put("privateComments", comments(received, true));
        entry.put("evaluationsReceived", received.size());
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
