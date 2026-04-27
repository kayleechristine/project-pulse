package edu.tcu.projectpulse.user;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.invitation.InvitationService;
import edu.tcu.projectpulse.invitation.InvitationToken;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

// Note: @Valid / bean validation (blank fields) is tested via GlobalExceptionHandlerTest
// and is enforced by Spring at the filter layer — not testable in plain unit tests.

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private InvitationService invitationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    private InvitationToken validToken;
    private RegisterRequest validRequest;

    @BeforeEach
    void setUp() {
        validToken = new InvitationToken();
        validToken.setToken("valid-token");
        validToken.setEmail("student@tcu.edu");
        validToken.setRole(UserRole.STUDENT);
        validToken.setCreatedAt(Instant.now());
        validToken.setExpiresAt(Instant.now().plusSeconds(86400));
        validToken.setUsed(false);

        validRequest = new RegisterRequest();
        validRequest.setToken("valid-token");
        validRequest.setFirstName("Jane");
        validRequest.setLastName("Doe");
        validRequest.setPassword("securepass123");
    }

    @Test
    void should_ReturnSuccess_When_RegistrationIsValid() {
        given(invitationService.validateToken("valid-token")).willReturn(validToken);
        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.empty());
        given(passwordEncoder.encode("securepass123")).willReturn("hashed-password");
        given(userService.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        Result result = userController.register(validRequest);

        assertThat(result.isFlag()).isTrue();
        assertThat(result.getCode()).isEqualTo(StatusCode.SUCCESS);
    }

    @Test
    void should_SaveUserWithCorrectFields_When_RegistrationIsValid() {
        given(invitationService.validateToken("valid-token")).willReturn(validToken);
        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.empty());
        given(passwordEncoder.encode("securepass123")).willReturn("hashed-password");
        given(userService.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        userController.register(validRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).save(captor.capture());

        User saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo("student@tcu.edu");
        assertThat(saved.getFirstName()).isEqualTo("Jane");
        assertThat(saved.getLastName()).isEqualTo("Doe");
        assertThat(saved.getPassword()).isEqualTo("hashed-password");
        assertThat(saved.getRoles()).contains(UserRole.STUDENT);
        assertThat(saved.isEnabled()).isTrue();
        assertThat(saved.getAccountStatus()).isEqualTo(User.AccountStatus.ACTIVE);
    }

    @Test
    void should_MarkTokenAsUsed_When_RegistrationIsValid() {
        given(invitationService.validateToken("valid-token")).willReturn(validToken);
        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.empty());
        given(passwordEncoder.encode(anyString())).willReturn("hashed-password");
        given(userService.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        userController.register(validRequest);

        verify(invitationService).markUsed("valid-token");
    }

    @Test
    void should_ThrowValidationException_When_TokenIsAlreadyUsed() {
        given(invitationService.validateToken("valid-token"))
                .willThrow(new ValidationException("Invitation token has already been used"));

        assertThatThrownBy(() -> userController.register(validRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("already been used");

        verify(userService, never()).save(any());
        verify(invitationService, never()).markUsed(anyString());
    }

    @Test
    void should_ThrowValidationException_When_TokenIsExpired() {
        given(invitationService.validateToken("valid-token"))
                .willThrow(new ValidationException("Invitation token has expired"));

        assertThatThrownBy(() -> userController.register(validRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("expired");

        verify(userService, never()).save(any());
        verify(invitationService, never()).markUsed(anyString());
    }

    @Test
    void should_ThrowResourceNotFoundException_When_TokenDoesNotExist() {
        given(invitationService.validateToken("valid-token"))
                .willThrow(new ResourceNotFoundException("InvitationToken", "token", "valid-token"));

        assertThatThrownBy(() -> userController.register(validRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userService, never()).save(any());
        verify(invitationService, never()).markUsed(anyString());
    }

    @Test
    void should_ThrowValidationException_When_UserIsAlreadyActive() {
        User existingUser = new User();
        existingUser.setEmail("student@tcu.edu");
        existingUser.setAccountStatus(User.AccountStatus.ACTIVE);

        given(invitationService.validateToken("valid-token")).willReturn(validToken);
        given(userService.findByEmail("student@tcu.edu")).willReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> userController.register(validRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("already exists");

        verify(userService, never()).save(any());
        verify(invitationService, never()).markUsed(anyString());
    }
}
