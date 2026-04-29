package edu.tcu.projectpulse.user;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.dto.UpdateProfileRequest;
import edu.tcu.projectpulse.user.dto.UserProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

// Note: @Valid / bean validation (blank fields) is tested via GlobalExceptionHandlerTest
// and is enforced by Spring at the filter layer — not testable in plain unit tests.

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User existingStudent;
    private UserDetails mockPrincipal;

    @BeforeEach
    void setUp() {
        existingStudent = new User();
        existingStudent.setEmail("student@tcu.edu");
        existingStudent.setFirstName("Jane");
        existingStudent.setLastName("Doe");
        existingStudent.setAccountStatus(User.AccountStatus.ACTIVE);
        existingStudent.setRoles(Set.of(UserRole.STUDENT));

        mockPrincipal = mock(UserDetails.class);
        lenient().when(mockPrincipal.getUsername()).thenReturn("student@tcu.edu");
    }

    // --- UC-26: Edit account ---

    @Test
    void should_ReturnProfile_When_GetProfileIsCalled() {
        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.of(existingStudent));

        Result result = userController.getProfile(mockPrincipal);

        assertThat(result.isFlag()).isTrue();
        assertThat(result.getCode()).isEqualTo(StatusCode.SUCCESS);
        UserProfileResponse profile = (UserProfileResponse) result.getData();
        assertThat(profile.getEmail()).isEqualTo("student@tcu.edu");
        assertThat(profile.getFirstName()).isEqualTo("Jane");
        assertThat(profile.getLastName()).isEqualTo("Doe");
    }

    @Test
    void should_ThrowResourceNotFoundException_When_GetProfileUserNotFound() {
        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.empty());

        assertThatThrownBy(() -> userController.getProfile(mockPrincipal))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void should_ReturnUpdatedProfile_When_UpdateIsValid() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("Janet");
        request.setLastName("Smith");
        request.setEmail("student@tcu.edu");

        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.of(existingStudent));
        given(userService.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        Result result = userController.updateProfile(mockPrincipal, request);

        assertThat(result.isFlag()).isTrue();
        assertThat(result.getCode()).isEqualTo(StatusCode.SUCCESS);
        UserProfileResponse profile = (UserProfileResponse) result.getData();
        assertThat(profile.getFirstName()).isEqualTo("Janet");
        assertThat(profile.getLastName()).isEqualTo("Smith");
    }

    @Test
    void should_SaveUpdatedFields_When_UpdateIsValid() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("Janet");
        request.setLastName("Smith");
        request.setEmail("student@tcu.edu");

        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.of(existingStudent));
        given(userService.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        userController.updateProfile(mockPrincipal, request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).save(captor.capture());
        assertThat(captor.getValue().getFirstName()).isEqualTo("Janet");
        assertThat(captor.getValue().getLastName()).isEqualTo("Smith");
    }

    @Test
    void should_ThrowValidationException_When_NewEmailIsAlreadyInUse() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("taken@tcu.edu");

        User otherUser = new User();
        otherUser.setEmail("taken@tcu.edu");

        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.of(existingStudent));
        given(userService.findByEmail("taken@tcu.edu")).willReturn(Optional.of(otherUser));

        assertThatThrownBy(() -> userController.updateProfile(mockPrincipal, request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("already in use");

        verify(userService, never()).save(any());
    }

    @Test
    void should_ThrowResourceNotFoundException_When_UpdateProfileUserNotFound() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("student@tcu.edu");

        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.empty());

        assertThatThrownBy(() -> userController.updateProfile(mockPrincipal, request))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userService, never()).save(any());
    }
}
