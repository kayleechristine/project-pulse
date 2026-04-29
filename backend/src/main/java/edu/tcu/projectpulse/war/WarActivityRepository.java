package edu.tcu.projectpulse.war;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarActivityRepository extends JpaRepository<WarActivity, Integer> {

    List<WarActivity> findByStudentIdAndWeekId(Integer studentId, Integer weekId);

    List<WarActivity> findByWeekId(Integer weekId);

    boolean existsByStudentIdAndWeekId(Integer studentId, Integer weekId);
}
