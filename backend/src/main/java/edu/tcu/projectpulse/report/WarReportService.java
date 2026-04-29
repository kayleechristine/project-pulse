package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.team.TeamMemberRepository;
import edu.tcu.projectpulse.war.WarActivity;
import edu.tcu.projectpulse.war.WarActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WarReportService {

    private final WarActivityRepository warActivityRepository;
    private final TeamMemberRepository teamMemberRepository;

    public WarReportService(WarActivityRepository warActivityRepository,
                            TeamMemberRepository teamMemberRepository) {
        this.warActivityRepository = warActivityRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    public Map<String, Object> buildTeamWarReport(Integer teamId, Integer weekId) {
        List<Integer> teamMemberIds = teamMemberRepository.findByTeamId(teamId).stream()
                .map(m -> m.getStudentId())
                .collect(Collectors.toList());

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
