package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.peerevaluation.PeerEvaluation;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluationRepository;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SectionPeerEvalReportServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PeerEvaluationRepository peerEvaluationRepository;

    @Mock
    private GradeCalculator gradeCalculator;

    @InjectMocks
    private SectionPeerEvalReportService service;

    @Test
    void should_FlagStudentWhoDidNotSubmitPeerEvaluationForWeek() {
        Team team = new Team();
        team.setStudentIds(Set.of(1, 2));

        PeerEvaluation received = new PeerEvaluation();
        received.setPublicComments("Helpful teammate");
        received.setPrivateComments("Needs more testing detail");

        given(teamRepository.findBySectionId(10L)).willReturn(List.of(team));
        given(userRepository.findAllById(Set.of(1, 2))).willReturn(List.of());
        given(peerEvaluationRepository.findByEvaluateeIdAndWeekId(1, 5)).willReturn(List.of(received));
        given(peerEvaluationRepository.findByEvaluatorIdAndWeekId(1, 5)).willReturn(List.of());
        given(peerEvaluationRepository.findByEvaluateeIdAndWeekId(2, 5)).willReturn(List.of());
        given(peerEvaluationRepository.findByEvaluatorIdAndWeekId(2, 5)).willReturn(List.of(received));
        given(gradeCalculator.calculate(List.of(received))).willReturn(4.0);

        Map<String, Object> report = service.generate(10L, 5);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> entries = (List<Map<String, Object>>) report.get("entries");
        Map<String, Object> firstStudent = entries.stream()
                .filter(entry -> entry.get("studentId").equals(1))
                .findFirst()
                .orElseThrow();

        assertThat(firstStudent.get("grade")).isEqualTo(4.0);
        assertThat(firstStudent.get("didNotSubmit")).isEqualTo(true);
        assertThat((List<String>) firstStudent.get("publicComments")).containsExactly("Helpful teammate");
        assertThat((List<String>) firstStudent.get("privateComments")).containsExactly("Needs more testing detail");
    }
}
