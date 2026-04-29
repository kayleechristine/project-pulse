package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.activeweek.ActiveWeek;
import edu.tcu.projectpulse.activeweek.ActiveWeekRepository;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluation;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluationRepository;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StudentPeerEvalReportServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ActiveWeekRepository activeWeekRepository;

    @Mock
    private PeerEvaluationRepository peerEvaluationRepository;

    @Mock
    private GradeCalculator gradeCalculator;

    @InjectMocks
    private StudentPeerEvalReportService service;

    @Test
    void should_ReturnPerWeekGradesAndCommentsForDateRange() {
        User student = new User();
        student.setId(3);
        student.setEmail("student@tcu.edu");

        Team team = new Team();
        team.setSectionId(20L);

        ActiveWeek week = new ActiveWeek();
        ReflectionTestUtils.setField(week, "id", 8L);
        week.setStartDate(LocalDate.of(2026, 1, 5));
        week.setEndDate(LocalDate.of(2026, 1, 11));

        PeerEvaluation evaluation = new PeerEvaluation();
        evaluation.setPublicComments("Great communicator");
        evaluation.setPrivateComments("Missed standup");

        given(userRepository.findById(3)).willReturn(Optional.of(student));
        given(teamRepository.findByStudentId(3)).willReturn(Optional.of(team));
        given(activeWeekRepository.findBySectionIdAndEndDateGreaterThanEqualAndStartDateLessThanEqualOrderByStartDateAsc(
                20L, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31))).willReturn(List.of(week));
        given(peerEvaluationRepository.findByEvaluateeIdAndWeekId(3, 8)).willReturn(List.of(evaluation));
        given(gradeCalculator.calculate(List.of(evaluation))).willReturn(5.0);

        Map<String, Object> report = service.generate(3, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> weeks = (List<Map<String, Object>>) report.get("weeks");
        assertThat(weeks).hasSize(1);
        assertThat(weeks.get(0).get("grade")).isEqualTo(5.0);
        assertThat((List<String>) weeks.get(0).get("publicComments")).containsExactly("Great communicator");
        assertThat((List<String>) weeks.get(0).get("privateComments")).containsExactly("Missed standup");
    }
}
