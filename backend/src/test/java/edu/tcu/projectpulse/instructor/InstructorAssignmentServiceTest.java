package edu.tcu.projectpulse.instructor;

import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InstructorAssignmentServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    private InstructorAssignmentService instructorAssignmentService;

    @BeforeEach
    void setUp() {
        instructorAssignmentService = new InstructorAssignmentService(teamRepository, userRepository);
    }

    @Test
    void should_MoveInstructorToTeam_When_InstructorAlreadyBelongsToAnotherTeam() {
        Team oldTeam = teamWithId(1L);
        oldTeam.setInstructorIds(new HashSet<>(Set.of(20, 21)));

        Team newTeam = teamWithId(2L);
        newTeam.setInstructorIds(new HashSet<>(Set.of(22)));

        given(teamRepository.findById(2L)).willReturn(Optional.of(newTeam));
        given(userRepository.findById(20)).willReturn(Optional.of(instructorWithId(20)));
        given(teamRepository.findAllByInstructorId(20)).willReturn(List.of(oldTeam));
        given(teamRepository.save(any(Team.class))).willAnswer(invocation -> invocation.getArgument(0));

        Team result = instructorAssignmentService.assign(2L, Set.of(20));

        assertThat(oldTeam.getInstructorIds()).containsExactly(21);
        assertThat(result.getInstructorIds()).containsExactlyInAnyOrder(20, 22);
        verify(teamRepository).save(oldTeam);
        verify(teamRepository).save(newTeam);
    }

    private User instructorWithId(Integer id) {
        User instructor = new User();
        instructor.setId(id);
        instructor.addRole(UserRole.INSTRUCTOR);
        return instructor;
    }

    private Team teamWithId(Long id) {
        Team team = new Team();
        ReflectionTestUtils.setField(team, "id", id);
        return team;
    }
}
