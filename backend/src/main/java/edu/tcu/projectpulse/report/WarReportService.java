package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.war.WarActivity;
import edu.tcu.projectpulse.war.WarActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WarReportService {

    private final WarActivityRepository warActivityRepository;

    public WarReportService(WarActivityRepository warActivityRepository) {
        this.warActivityRepository = warActivityRepository;
    }

    public Map<String, Object> buildTeamWarReport(Integer teamId, Integer weekId) {
        // TODO: replace stub with real team member lookup (needs TeamMemberRepository)
        List<Integer> teamMemberIds = List.of();

        List<Map<String, Object>> entries = teamMemberIds.stream()
                .map(studentId -> {
                    List<WarActivity> activities =
                            warActivityRepository.findByStudentIdAndWeekId(studentId, weekId);
                    return Map.<String, Object>of(
                            "studentId", studentId,
                            "submitted", !activities.isEmpty(),
                            "activities", activities
                    );
                })
                .collect(Collectors.toList());

        return Map.of(
                "teamId", teamId,
                "weekId", weekId,
                "entries", entries
        );
    }
}
