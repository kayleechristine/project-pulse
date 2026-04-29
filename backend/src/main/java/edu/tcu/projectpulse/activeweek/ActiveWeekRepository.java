package edu.tcu.projectpulse.activeweek;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActiveWeekRepository extends JpaRepository<ActiveWeek, Long> {
    List<ActiveWeek> findBySectionIdOrderByStartDateAsc(Long sectionId);
    void deleteBySectionId(Long sectionId);
}