package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.team.TeamMember;
import edu.tcu.projectpulse.team.TeamMemberRepository;
import edu.tcu.projectpulse.war.WarActivity;
import edu.tcu.projectpulse.war.WarActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WarReportServiceTest {

    @Mock
    private WarActivityRepository warActivityRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @InjectMocks
    private WarReportService warReportService;

    private TeamMember member(Integer teamId, Integer studentId) {
        TeamMember m = new TeamMember();
        m.setTeamId(teamId);
        m.setStudentId(studentId);
        return m;
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
        given(teamMemberRepository.findByTeamId(1)).willReturn(List.of(
                member(1, 10),
                member(1, 11)
        ));
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
        given(teamMemberRepository.findByTeamId(99)).willReturn(List.of());

        Map<String, Object> report = warReportService.buildTeamWarReport(99, 1);

        assertThat(report.get("teamId")).isEqualTo(99);
        assertThat(report.get("weekId")).isEqualTo(1);
        assertThat((List<?>) report.get("entries")).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_MarkAllSubmitted_When_AllMembersHaveActivities() {
        given(teamMemberRepository.findByTeamId(2)).willReturn(List.of(
                member(2, 20),
                member(2, 21)
        ));
        given(warActivityRepository.findByStudentIdAndWeekId(20, 3)).willReturn(List.of(activity(20, 3)));
        given(warActivityRepository.findByStudentIdAndWeekId(21, 3)).willReturn(List.of(activity(21, 3)));

        Map<String, Object> report = warReportService.buildTeamWarReport(2, 3);

        List<Map<String, Object>> entries = (List<Map<String, Object>>) report.get("entries");
        assertThat(entries).allMatch(e -> (Boolean) e.get("submitted"));
    }
}
