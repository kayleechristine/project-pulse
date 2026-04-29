package edu.tcu.projectpulse.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByNameIgnoreCase(String name);

    @Query("SELECT t FROM Team t WHERE :studentId MEMBER OF t.studentIds")
    Optional<Team> findByStudentId(@Param("studentId") Integer studentId);
}