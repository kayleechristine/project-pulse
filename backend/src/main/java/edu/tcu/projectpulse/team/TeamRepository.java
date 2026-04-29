package edu.tcu.projectpulse.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByNameIgnoreCase(String name);

    java.util.List<Team> findBySectionId(Long sectionId);

    @Query("SELECT t FROM Team t WHERE :studentId MEMBER OF t.studentIds")
    Optional<Team> findByStudentId(@Param("studentId") Integer studentId);

    @Query("SELECT t FROM Team t WHERE :studentId MEMBER OF t.studentIds")
    java.util.List<Team> findAllByStudentId(@Param("studentId") Integer studentId);

    @Query("SELECT t FROM Team t WHERE :instructorId MEMBER OF t.instructorIds")
    java.util.List<Team> findAllByInstructorId(@Param("instructorId") Integer instructorId);
}
