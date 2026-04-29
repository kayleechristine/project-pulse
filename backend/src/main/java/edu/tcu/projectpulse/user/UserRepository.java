package edu.tcu.projectpulse.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByInvitationToken(String invitationToken);

    @Query("""
            SELECT DISTINCT u FROM User u JOIN u.roles r
            WHERE r = :role
              AND (
                :query IS NULL OR :query = '' OR
                LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
              )
            ORDER BY u.lastName, u.firstName, u.email
            """)
    List<User> searchByRole(@Param("role") UserRole role, @Param("query") String query);
}
