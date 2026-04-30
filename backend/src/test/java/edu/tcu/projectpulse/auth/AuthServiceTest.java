package edu.tcu.projectpulse.auth;

import edu.tcu.projectpulse.auth.dto.LoginRequest;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.invitation.InvitationService;
import edu.tcu.projectpulse.invitation.InvitationToken;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRole;
import edu.tcu.projectpulse.user.dto.RegisterRequest;
import edu.tcu.projectpulse.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private InvitationService invitationService;

    @InjectMocks
    private AuthService authService;

    private User activeUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        activeUser = new User();
        activeUser.setEmail("user@test.com");
        activeUser.setPassword("hashed-password");
        activeUser.setFirstName("Test");
        activeUser.setLastName("User");
        activeUser.setEnabled(true);
        activeUser.setAccountStatus(User.AccountStatus.ACTIVE);
        activeUser.addRole(UserRole.STUDENT);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("user@test.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void should_ReturnSuccessResult_When_CredentialsAreValid() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "user@test.com", "hashed-password",
                List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
        );

        given(userService.findByEmail("user@test.com")).willReturn(Optional.of(activeUser));
        given(passwordEncoder.matches("password123", "hashed-password")).willReturn(true);
        given(userService.loadUserByUsername("user@test.com")).willReturn(userDetails);
        given(jwtTokenProvider.generateToken("user@test.com", userDetails.getAuthorities()))
                .willReturn("mock-jwt-token");

        Result result = authService.login(loginRequest);

        assertThat(result.isFlag()).isTrue();
        assertThat(result.getCode()).isEqualTo(StatusCode.SUCCESS);
        assertThat(result.getMessage()).isEqualTo("Login successful");
        assertThat(result.getData()).isNotNull();
    }

    @Test
    void should_ThrowBadCredentialsException_When_EmailNotFound() {
        given(userService.findByEmail("user@test.com")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void should_ThrowBadCredentialsException_When_PasswordIsIncorrect() {
        given(userService.findByEmail("user@test.com")).willReturn(Optional.of(activeUser));
        given(passwordEncoder.matches("password123", "hashed-password")).willReturn(false);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void should_ThrowValidationException_When_AccountIsNotActive() {
        activeUser.setAccountStatus(User.AccountStatus.INVITED);

        given(userService.findByEmail("user@test.com")).willReturn(Optional.of(activeUser));
        given(passwordEncoder.matches("password123", "hashed-password")).willReturn(true);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("not active");
    }

    @Test
    void should_ThrowValidationException_When_AccountIsDeactivated() {
        activeUser.setAccountStatus(User.AccountStatus.DEACTIVATED);

        given(userService.findByEmail("user@test.com")).willReturn(Optional.of(activeUser));
        given(passwordEncoder.matches("password123", "hashed-password")).willReturn(true);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("not active");
    }

    @Test
    void should_RegisterInstructor_When_InvitationIsValid() {
        InvitationToken token = new InvitationToken();
        token.setToken("valid-token");
        token.setEmail("instructor@tcu.edu");
        token.setRole(UserRole.INSTRUCTOR);
        token.setCreatedAt(Instant.now());
        token.setExpiresAt(Instant.now().plusSeconds(3600));

        RegisterRequest request = new RegisterRequest();
        request.setToken("valid-token");
        request.setFirstName("Ada");
        request.setLastName("Lovelace");
        request.setPassword("password123");

        given(invitationService.validateToken("valid-token")).willReturn(token);
        given(userService.findByEmail("instructor@tcu.edu")).willReturn(Optional.empty());
        given(passwordEncoder.encode("password123")).willReturn("hashed-password");
        given(userService.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        Result result = authService.register(request);

        assertThat(result.isFlag()).isTrue();
        verify(invitationService).markUsed("valid-token");
    }
}
