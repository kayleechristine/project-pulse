package edu.tcu.projectpulse.activeweek;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActiveWeekRepository extends JpaRepository<ActiveWeek, Integer> {

    Optional<ActiveWeek> findTopBySectionIdAndActiveTrueOrderByWeekStartDesc(Integer sectionId);

}