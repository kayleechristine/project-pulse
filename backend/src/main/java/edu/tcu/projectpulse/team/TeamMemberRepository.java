package edu.tcu.projectpulse.team;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Integer> {

    boolean existsByTeamIdAndStudentId(Integer teamId, Integer studentId);

}