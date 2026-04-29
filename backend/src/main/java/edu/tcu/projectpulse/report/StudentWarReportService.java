package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.activeweek.ActiveWeek;
import edu.tcu.projectpulse.activeweek.ActiveWeekRepository;
import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.war.WarActivity;
import edu.tcu.projectpulse.war.WarActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentWarReportService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ActiveWeekRepository activeWeekRepository;
    private final WarActivityRepository warActivityRepository;

    public StudentWarReportService(UserRepository userRepository,
                                   TeamRepository teamRepository,
                                   ActiveWeekRepository activeWeekRepository,
                                   WarActivityRepository warActivityRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.activeWeekRepository = activeWeekRepository;
        this.warActivityRepository = warActivityRepository;
    }

    public Map<String, Object> generate(Integer studentId, LocalDate startDate, LocalDate endDate) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", studentId));
        Team team = teamRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "studentId", studentId));

        List<ActiveWeek> activeWeeks = activeWeekRepository
                .findBySectionIdAndEndDateGreaterThanEqualAndStartDateLessThanEqualOrderByStartDateAsc(
                        team.getSectionId(), startDate, endDate);
        List<Integer> weekIds = activeWeeks.stream().map(week -> week.getId().intValue()).toList();
        Map<Integer, List<WarActivity>> activitiesByWeek = warActivityRepository
                .findByStudentIdAndWeekIdIn(studentId, weekIds)
                .stream()
                .collect(Collectors.groupingBy(WarActivity::getWeekId));

        List<Map<String, Object>> weeks = activeWeeks.stream()
                .map(week -> buildWeekEntry(week, activitiesByWeek.getOrDefault(week.getId().intValue(), List.of())))
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

    private Map<String, Object> buildWeekEntry(ActiveWeek week, List<WarActivity> activities) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("weekId", week.getId());
        entry.put("startDate", week.getStartDate());
        entry.put("endDate", week.getEndDate());
        entry.put("submitted", !activities.isEmpty());
        entry.put("activities", activities);
        entry.put("plannedHours", activities.stream().mapToDouble(WarActivity::getPlannedHours).sum());
        entry.put("actualHours", activities.stream().mapToDouble(activity -> activity.getActualHours() == null ? 0.0 : activity.getActualHours()).sum());
        return entry;
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
