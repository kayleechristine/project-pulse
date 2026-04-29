package edu.tcu.projectpulse.student;

import edu.tcu.projectpulse.invitation.InvitationService;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluation;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluationRepository;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.user.UserRole;
import edu.tcu.projectpulse.war.WarActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvitationService invitationService;

    @Mock
    private WarActivityRepository warActivityRepository;

    @Mock
    private PeerEvaluationRepository peerEvaluationRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void should_DeleteStudentAndAssociatedWarPeerEvaluationsAndTeamMembership() {
        User student = new User();
        student.setId(7);
        student.addRole(UserRole.STUDENT);

        Team team = new Team();
        team.setStudentIds(new java.util.HashSet<>(Set.of(7, 8)));

        PeerEvaluation evaluation = new PeerEvaluation();
        evaluation.setEvaluatorId(7);
        evaluation.setEvaluateeId(8);

        given(userRepository.findById(7)).willReturn(Optional.of(student));
        given(teamRepository.findAllByStudentId(7)).willReturn(List.of(team));
        given(peerEvaluationRepository.findByEvaluatorIdOrEvaluateeId(7, 7)).willReturn(List.of(evaluation));

        studentService.delete(7);

        assertThat(team.getStudentIds()).doesNotContain(7);
        verify(teamRepository).save(team);
        verify(peerEvaluationRepository).deleteAll(List.of(evaluation));
        verify(warActivityRepository).deleteByStudentId(7);
        verify(userRepository).delete(student);
    }
}
