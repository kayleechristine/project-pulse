package edu.tcu.projectpulse.invitation;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRole;
import edu.tcu.projectpulse.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    private InvitationTokenRepository tokenRepository;

    @Mock
    private UserService userService;

    @Mock
    private InvitationEmailService invitationEmailService;

    @InjectMocks
    private InvitationService invitationService;

    private InvitationToken validToken;

    @BeforeEach
    void setUp() {
        validToken = new InvitationToken();
        validToken.setToken("valid-uuid-token");
        validToken.setEmail("newuser@test.com");
        validToken.setRole(UserRole.STUDENT);
        validToken.setCreatedAt(Instant.now());
        validToken.setExpiresAt(Instant.now().plusSeconds(86400));
        validToken.setUsed(false);
    }

    @Test
    void should_ReturnSavedToken_When_EmailAndRoleAreValid() {
        given(userService.findByEmail("newuser@test.com")).willReturn(Optional.empty());
        given(tokenRepository.save(any(InvitationToken.class))).willReturn(validToken);

        InvitationToken result = invitationService.generateToken("newuser@test.com", UserRole.STUDENT);

        ArgumentCaptor<InvitationToken> captor = ArgumentCaptor.forClass(InvitationToken.class);
        verify(tokenRepository).save(captor.capture());

        InvitationToken saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo("newuser@test.com");
        assertThat(saved.getRole()).isEqualTo(UserRole.STUDENT);
        assertThat(saved.isUsed()).isFalse();
        assertThat(saved.getToken()).isNotBlank();
        assertThat(saved.getExpiresAt()).isAfter(Instant.now());
        assertThat(result).isEqualTo(validToken);
        verify(invitationEmailService).sendInvitation(validToken);
    }

    @Test
    void should_SaveSectionId_When_StudentInvitationTargetsASection() {
        given(userService.findByEmail("newuser@test.com")).willReturn(Optional.empty());
        given(tokenRepository.save(any(InvitationToken.class))).willReturn(validToken);

        invitationService.generateToken("newuser@test.com", UserRole.STUDENT, 42L);

        ArgumentCaptor<InvitationToken> captor = ArgumentCaptor.forClass(InvitationToken.class);
        verify(tokenRepository).save(captor.capture());
        assertThat(captor.getValue().getSectionId()).isEqualTo(42L);
    }

    @Test
    void should_ThrowValidationException_When_RoleIsAdmin() {
        assertThatThrownBy(() -> invitationService.generateToken("admin@test.com", UserRole.ADMIN))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("ADMIN");
    }

    @Test
    void should_ThrowValidationException_When_EmailAlreadyExists() {
        given(userService.findByEmail("newuser@test.com")).willReturn(Optional.of(new User()));

        assertThatThrownBy(() -> invitationService.generateToken("newuser@test.com", UserRole.STUDENT))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void should_ReturnToken_When_TokenIsValid() {
        given(tokenRepository.findByToken("valid-uuid-token")).willReturn(Optional.of(validToken));

        InvitationToken result = invitationService.validateToken("valid-uuid-token");

        assertThat(result).isEqualTo(validToken);
    }

    @Test
    void should_ThrowResourceNotFoundException_When_TokenDoesNotExist() {
        given(tokenRepository.findByToken("unknown-token")).willReturn(Optional.empty());

        assertThatThrownBy(() -> invitationService.validateToken("unknown-token"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void should_ThrowValidationException_When_TokenIsAlreadyUsed() {
        validToken.setUsed(true);
        given(tokenRepository.findByToken("valid-uuid-token")).willReturn(Optional.of(validToken));

        assertThatThrownBy(() -> invitationService.validateToken("valid-uuid-token"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("already been used");
    }

    @Test
    void should_ThrowValidationException_When_TokenIsExpired() {
        validToken.setExpiresAt(Instant.now().minusSeconds(1));
        given(tokenRepository.findByToken("valid-uuid-token")).willReturn(Optional.of(validToken));

        assertThatThrownBy(() -> invitationService.validateToken("valid-uuid-token"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("expired");
    }

    @Test
    void should_MarkTokenAsUsed_When_MarkUsedIsCalled() {
        given(tokenRepository.findByToken("valid-uuid-token")).willReturn(Optional.of(validToken));
        given(tokenRepository.save(any(InvitationToken.class))).willReturn(validToken);

        invitationService.markUsed("valid-uuid-token");

        ArgumentCaptor<InvitationToken> captor = ArgumentCaptor.forClass(InvitationToken.class);
        verify(tokenRepository).save(captor.capture());
        assertThat(captor.getValue().isUsed()).isTrue();
    }
}
