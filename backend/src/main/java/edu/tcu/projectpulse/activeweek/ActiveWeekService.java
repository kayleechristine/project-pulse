package edu.tcu.projectpulse.activeweek;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ActiveWeekService {

    private final ActiveWeekRepository activeWeekRepository;

    public ActiveWeekService(ActiveWeekRepository activeWeekRepository) {
        this.activeWeekRepository = activeWeekRepository;
    }

    public List<ActiveWeek> getWeeksForSection(Long sectionId) {
        return activeWeekRepository
                .findBySectionIdAndActiveTrueAndStartDateLessThanEqualOrderByStartDateDesc(sectionId, LocalDate.now());
    }
}
