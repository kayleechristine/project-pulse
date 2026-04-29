package edu.tcu.projectpulse.instructor;

import edu.tcu.projectpulse.invitation.InvitationService;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.user.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvitationService invitationService;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private InstructorService instructorService;

    @Test
    void should_DeactivateAndReactivateInstructor() {
        User instructor = new User();
        instructor.setId(5);
        instructor.addRole(UserRole.INSTRUCTOR);
        instructor.setEnabled(true);
        instructor.setAccountStatus(User.AccountStatus.ACTIVE);

        given(userRepository.findById(5)).willReturn(Optional.of(instructor));
        given(userRepository.save(instructor)).willReturn(instructor);

        User deactivated = instructorService.deactivate(5);

        assertThat(deactivated.isEnabled()).isFalse();
        assertThat(deactivated.getAccountStatus()).isEqualTo(User.AccountStatus.DEACTIVATED);

        User reactivated = instructorService.reactivate(5);

        assertThat(reactivated.isEnabled()).isTrue();
        assertThat(reactivated.getAccountStatus()).isEqualTo(User.AccountStatus.ACTIVE);
    }
}
