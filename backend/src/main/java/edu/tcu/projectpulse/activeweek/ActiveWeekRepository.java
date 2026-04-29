package edu.tcu.projectpulse.activeweek;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ActiveWeekRepository extends JpaRepository<ActiveWeek, Integer> {
    List<ActiveWeek> findBySectionIdOrderByStartDateAsc(Long sectionId);
    void deleteBySectionId(Long sectionId);
    List<ActiveWeek> findBySectionIdAndActiveTrueAndStartDateLessThanEqualOrderByStartDateDesc(Long sectionId, LocalDate today);
}
