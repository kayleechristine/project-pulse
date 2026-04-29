package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.war.WarActivity;
import edu.tcu.projectpulse.war.WarActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WarReportService {

    private final WarActivityRepository warActivityRepository;
    private final TeamRepository teamRepository;

    public WarReportService(WarActivityRepository warActivityRepository,
                            TeamRepository teamRepository) {
        this.warActivityRepository = warActivityRepository;
        this.teamRepository = teamRepository;
    }

    public Map<String, Object> buildTeamWarReport(Integer teamId, Integer weekId) {
        Team team = teamRepository.findById(teamId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));

        List<Map<String, Object>> entries = team.getStudentIds().stream()
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
