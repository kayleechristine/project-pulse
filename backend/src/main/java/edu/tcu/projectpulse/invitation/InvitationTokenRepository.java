package edu.tcu.projectpulse.invitation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationTokenRepository extends JpaRepository<InvitationToken, Long> {

    Optional<InvitationToken> findByToken(String token);

    Optional<InvitationToken> findByEmailAndUsedFalse(String email);
}
