package edu.tcu.projectpulse.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Integer> {

    boolean existsByTeamIdAndStudentId(Integer teamId, Integer studentId);

    Optional<TeamMember> findByStudentId(Integer studentId);

    List<TeamMember> findByTeamId(Integer teamId);

}