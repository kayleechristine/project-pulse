package edu.tcu.projectpulse.exception;

import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void should_Return404Result_When_ResourceNotFoundExceptionThrown() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User", "id", 42);

        Result result = handler.handleResourceNotFound(ex);

        assertThat(result.isFlag()).isFalse();
        assertThat(result.getCode()).isEqualTo(StatusCode.NOT_FOUND);
        assertThat(result.getMessage()).contains("User").contains("42");
    }

    @Test
    void should_Return400Result_When_ValidationExceptionThrown() {
        ValidationException ex = new ValidationException("Email must not be empty");

        Result result = handler.handleValidation(ex);

        assertThat(result.isFlag()).isFalse();
        assertThat(result.getCode()).isEqualTo(StatusCode.INVALID_ARGUMENT);
        assertThat(result.getMessage()).isEqualTo("Email must not be empty");
    }

    @Test
    void should_Return401Result_When_BadCredentialsExceptionThrown() {
        BadCredentialsException ex = new BadCredentialsException("bad creds");

        Result result = handler.handleBadCredentials(ex);

        assertThat(result.isFlag()).isFalse();
        assertThat(result.getCode()).isEqualTo(StatusCode.UNAUTHORIZED);
        assertThat(result.getMessage()).isEqualTo("Invalid email or password");
        assertThat(result.getData()).isNull();
    }

    @Test
    void should_Return403Result_When_AccessDeniedExceptionThrown() {
        AccessDeniedException ex = new AccessDeniedException("forbidden");

        Result result = handler.handleAccessDenied(ex);

        assertThat(result.isFlag()).isFalse();
        assertThat(result.getCode()).isEqualTo(StatusCode.FORBIDDEN);
        assertThat(result.getMessage()).isEqualTo("Access denied");
    }

    @Test
    void should_Return500Result_When_UnexpectedExceptionThrown() {
        Exception ex = new RuntimeException("something went wrong");

        Result result = handler.handleGeneral(ex);

        assertThat(result.isFlag()).isFalse();
        assertThat(result.getCode()).isEqualTo(StatusCode.INTERNAL_SERVER_ERROR);
        assertThat(result.getMessage()).isEqualTo("An unexpected error occurred");
        assertThat(result.getData()).isNull();
    }
}
