package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.war.WarActivity;
import edu.tcu.projectpulse.war.WarActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WarReportServiceTest {

    @Mock
    private WarActivityRepository warActivityRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private WarReportService warReportService;

    private Team team(Integer id, Set<Integer> studentIds) {
        Team t = new Team();
        t.setStudentIds(studentIds);
        return t;
    }

    private WarActivity activity(Integer studentId, Integer weekId) {
        WarActivity a = new WarActivity();
        a.setStudentId(studentId);
        a.setWeekId(weekId);
        return a;
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_FlagNonSubmitter_When_StudentHasNoActivities() {
        given(teamRepository.findById(1L)).willReturn(Optional.of(team(1, Set.of(10, 11))));
        given(warActivityRepository.findByStudentIdAndWeekId(10, 5)).willReturn(List.of(activity(10, 5)));
        given(warActivityRepository.findByStudentIdAndWeekId(11, 5)).willReturn(List.of());

        Map<String, Object> report = warReportService.buildTeamWarReport(1, 5);

        List<Map<String, Object>> entries = (List<Map<String, Object>>) report.get("entries");
        assertThat(entries).hasSize(2);

        Map<String, Object> submitter = entries.stream()
                .filter(e -> e.get("studentId").equals(10)).findFirst().orElseThrow();
        assertThat(submitter.get("submitted")).isEqualTo(true);

        Map<String, Object> nonSubmitter = entries.stream()
                .filter(e -> e.get("studentId").equals(11)).findFirst().orElseThrow();
        assertThat(nonSubmitter.get("submitted")).isEqualTo(false);
    }

    @Test
    void should_ReturnEmptyEntries_When_TeamHasNoMembers() {
        given(teamRepository.findById(99L)).willReturn(Optional.of(team(99, Set.of())));

        Map<String, Object> report = warReportService.buildTeamWarReport(99, 1);

        assertThat(report.get("teamId")).isEqualTo(99);
        assertThat(report.get("weekId")).isEqualTo(1);
        assertThat((List<?>) report.get("entries")).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_MarkAllSubmitted_When_AllMembersHaveActivities() {
        given(teamRepository.findById(2L)).willReturn(Optional.of(team(2, Set.of(20, 21))));
        given(warActivityRepository.findByStudentIdAndWeekId(20, 3)).willReturn(List.of(activity(20, 3)));
        given(warActivityRepository.findByStudentIdAndWeekId(21, 3)).willReturn(List.of(activity(21, 3)));

        Map<String, Object> report = warReportService.buildTeamWarReport(2, 3);

        List<Map<String, Object>> entries = (List<Map<String, Object>>) report.get("entries");
        assertThat(entries).allMatch(e -> (Boolean) e.get("submitted"));
    }
}
