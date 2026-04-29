package edu.tcu.projectpulse.team;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeamController teamController;

    private UserDetails mockPrincipal;
    private User student;
    private Team team;

    @BeforeEach
    void setUp() {
        mockPrincipal = mock(UserDetails.class);
        given(mockPrincipal.getUsername()).willReturn("student@tcu.edu");

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
}
