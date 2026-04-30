package edu.tcu.projectpulse.team;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.instructor.InstructorAssignmentService;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.user.UserRole;
import edu.tcu.projectpulse.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InstructorAssignmentService instructorAssignmentService;

    @InjectMocks
    private TeamController teamController;

    private UserDetails mockPrincipal;
    private User student;
    private Team team;

    @BeforeEach
    void setUp() {
        mockPrincipal = mock(UserDetails.class);
        lenient().when(mockPrincipal.getUsername()).thenReturn("student@tcu.edu");

        student = new User();
        student.setId(1);
        student.setEmail("student@tcu.edu");
        student.setFirstName("Jane");
        student.setLastName("Doe");

        team = new Team();
        team.setStudentIds(Set.of(1, 10, 11));
        team.setSectionId(2L);
    }

    @Test
    void should_ReturnTeamWithTeammates_When_StudentIsOnATeam() {
        User teammate1 = new User();
        teammate1.setId(10);
        teammate1.setFirstName("John");
        teammate1.setLastName("Smith");

        User teammate2 = new User();
        teammate2.setId(11);
        teammate2.setFirstName("Lily");
        teammate2.setLastName("Fisher");

        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.of(student));
        given(teamRepository.findByStudentId(1)).willReturn(Optional.of(team));
        given(userRepository.findAllById(anyList())).willReturn(List.of(teammate1, teammate2));

        Result result = teamController.getMyTeam(mockPrincipal);

        assertThat(result.isFlag()).isTrue();
        assertThat(result.getCode()).isEqualTo(StatusCode.SUCCESS);

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.getData();
        assertThat(data.get("sectionId")).isEqualTo(2L);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> teammates = (List<Map<String, Object>>) data.get("teammates");
        assertThat(teammates).hasSize(2);
        assertThat(teammates).extracting(m -> m.get("firstName"))
                .containsExactlyInAnyOrder("John", "Lily");
    }

    @Test
    void should_ExcludeCurrentStudent_From_TeammateList() {
        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.of(student));
        given(teamRepository.findByStudentId(1)).willReturn(Optional.of(team));
        given(userRepository.findAllById(anyList())).willReturn(List.of());

        Result result = teamController.getMyTeam(mockPrincipal);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> teammates = (List<Map<String, Object>>)
                ((Map<String, Object>) result.getData()).get("teammates");
        assertThat(teammates).noneMatch(m -> m.get("id").equals(1));
    }

    @Test
    void should_ThrowResourceNotFoundException_When_StudentNotFound() {
        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.empty());

        assertThatThrownBy(() -> teamController.getMyTeam(mockPrincipal))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("student@tcu.edu");
    }

    @Test
    void should_ThrowResourceNotFoundException_When_StudentHasNoTeam() {
        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.of(student));
        given(teamRepository.findByStudentId(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> teamController.getMyTeam(mockPrincipal))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Team");
    }

    @Test
    void should_AssignInstructorToTeam_When_InstructorExists() {
        User instructor = new User();
        instructor.setId(20);
        instructor.addRole(UserRole.INSTRUCTOR);

        team.setInstructorIds(new java.util.HashSet<>(Set.of(20)));
        given(instructorAssignmentService.assign(1L, Set.of(20))).willReturn(team);

        Team result = teamController.assignInstructorsToTeam(
                1L,
                new TeamController.InstructorAssignmentRequest(Set.of(20))
        );

        assertThat(result.getInstructorIds()).contains(20);
        verify(instructorAssignmentService).assign(1L, Set.of(20));
    }

    @Test
    void should_RemoveInstructorFromTeam() {
        team.setInstructorIds(new java.util.HashSet<>(Set.of(20, 21)));

        Team updated = new Team();
        updated.setInstructorIds(new java.util.HashSet<>(Set.of(21)));
        given(instructorAssignmentService.remove(1L, 20)).willReturn(updated);

        Team result = teamController.removeInstructorFromTeam(1L, 20);

        assertThat(result.getInstructorIds()).containsExactly(21);
    }

    @Test
    void should_AssignStudentsToTeam_When_RequestContainsStudentIds() {
        Team existing = teamWithId(1L);
        existing.setStudentIds(new java.util.HashSet<>(Set.of(1)));

        User studentTwo = studentWithId(2);
        User studentThree = studentWithId(3);
        given(teamRepository.findById(1L)).willReturn(Optional.of(existing));
        given(userRepository.findById(2)).willReturn(Optional.of(studentTwo));
        given(userRepository.findById(3)).willReturn(Optional.of(studentThree));
        given(teamRepository.findAllByStudentId(2)).willReturn(List.of());
        given(teamRepository.findAllByStudentId(3)).willReturn(List.of());
        given(teamRepository.save(existing)).willReturn(existing);

        Team result = teamController.assignStudentsToTeam(
                1L,
                new TeamController.StudentAssignmentRequest(Set.of(2, 3))
        );

        assertThat(result.getStudentIds()).containsExactlyInAnyOrder(1, 2, 3);
        verify(teamRepository).save(existing);
    }

    @Test
    void should_MoveStudentToTeam_When_StudentAlreadyBelongsToAnotherTeam() {
        Team oldTeam = teamWithId(1L);
        oldTeam.setStudentIds(new java.util.HashSet<>(Set.of(2, 9)));

        Team newTeam = teamWithId(2L);
        newTeam.setStudentIds(new java.util.HashSet<>(Set.of(4)));

        given(teamRepository.findById(2L)).willReturn(Optional.of(newTeam));
        given(userRepository.findById(2)).willReturn(Optional.of(studentWithId(2)));
        given(teamRepository.findAllByStudentId(2)).willReturn(List.of(oldTeam));
        given(teamRepository.save(any(Team.class))).willAnswer(invocation -> invocation.getArgument(0));

        Team result = teamController.assignStudentsToTeam(
                2L,
                new TeamController.StudentAssignmentRequest(Set.of(2))
        );

        assertThat(oldTeam.getStudentIds()).containsExactly(9);
        assertThat(result.getStudentIds()).containsExactlyInAnyOrder(2, 4);
        verify(teamRepository).save(oldTeam);
        verify(teamRepository).save(newTeam);
    }

    private User studentWithId(Integer id) {
        User student = new User();
        student.setId(id);
        student.addRole(UserRole.STUDENT);
        return student;
    }

    private Team teamWithId(Long id) {
        Team team = new Team();
        ReflectionTestUtils.setField(team, "id", id);
        return team;
    }
}
