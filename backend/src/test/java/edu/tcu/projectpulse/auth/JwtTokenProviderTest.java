package edu.tcu.projectpulse.auth;

import edu.tcu.projectpulse.config.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setSecretKey("cHJvamVjdC1wdWxzZS1zZWNyZXQta2V5LWZvci1qd3QtYXV0aGVudGljYXRpb24=");
        jwtConfig.setTokenExpiration(86400000L);
        jwtTokenProvider = new JwtTokenProvider(jwtConfig);
    }

    @Test
    void should_GenerateNonBlankToken_When_EmailAndAuthoritiesProvided() {
        String token = jwtTokenProvider.generateToken(
                "user@test.com",
                List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
        );

        assertThat(token).isNotBlank();
    }

    @Test
    void should_ExtractCorrectEmail_When_TokenIsValid() {
        String token = jwtTokenProvider.generateToken(
                "user@test.com",
                List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
        );

        assertThat(jwtTokenProvider.getEmailFromToken(token)).isEqualTo("user@test.com");
    }

    @Test
    void should_ReturnTrue_When_TokenIsValid() {
        String token = jwtTokenProvider.generateToken(
                "user@test.com",
                List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
        );

        assertThat(jwtTokenProvider.isTokenValid(token)).isTrue();
    }

    @Test
    void should_ReturnFalse_When_TokenIsTampered() {
        String token = jwtTokenProvider.generateToken(
                "user@test.com",
                List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
        );
        String tampered = token.substring(0, token.length() - 4) + "XXXX";

        assertThat(jwtTokenProvider.isTokenValid(tampered)).isFalse();
    }

    @Test
    void should_ReturnFalse_When_TokenIsExpired() throws InterruptedException {
        JwtConfig shortConfig = new JwtConfig();
        shortConfig.setSecretKey("cHJvamVjdC1wdWxzZS1zZWNyZXQta2V5LWZvci1qd3QtYXV0aGVudGljYXRpb24=");
        shortConfig.setTokenExpiration(1L);
        JwtTokenProvider shortLivedProvider = new JwtTokenProvider(shortConfig);

        String token = shortLivedProvider.generateToken(
                "user@test.com",
                List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
        );

        Thread.sleep(10);

        assertThat(shortLivedProvider.isTokenValid(token)).isFalse();
    }

    @Test
    void should_ReturnFalse_When_TokenIsBlank() {
        assertThat(jwtTokenProvider.isTokenValid("")).isFalse();
    }
}
