package edu.tcu.projectpulse.activeweek;

import edu.tcu.projectpulse.section.Section;
import edu.tcu.projectpulse.section.SectionRepository;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/active-weeks")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ActiveWeekController {

    private final ActiveWeekRepository activeWeekRepository;
    private final SectionRepository sectionRepository;
    private final ActiveWeekService activeWeekService;

    public ActiveWeekController(ActiveWeekRepository activeWeekRepository,
                                SectionRepository sectionRepository,
                                ActiveWeekService activeWeekService) {
        this.activeWeekRepository = activeWeekRepository;
        this.sectionRepository = sectionRepository;
        this.activeWeekService = activeWeekService;
    }

    @GetMapping("/section/{sectionId}")
    public List<ActiveWeek> getActiveWeeksBySection(@PathVariable Long sectionId) {
        List<ActiveWeek> savedWeeks = activeWeekRepository.findBySectionIdOrderByStartDateAsc(sectionId);

        if (!savedWeeks.isEmpty()) {
            return savedWeeks;
        }

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));

        return generateWeeks(section);
    }

    @GetMapping("/section/{sectionId}/available")
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
    public Result getAvailableWeeksForSection(@PathVariable Long sectionId) {
        List<ActiveWeek> weeks = activeWeekService.getWeeksForSection(sectionId);
        return new Result(true, StatusCode.SUCCESS, "Active weeks retrieved.", weeks);
    }

    @PostMapping("/section/{sectionId}")
    @Transactional
    public List<ActiveWeek> saveActiveWeeks(@PathVariable Long sectionId,
                                            @RequestBody List<ActiveWeek> weeks) {
        if (!sectionRepository.existsById(sectionId)) {
            throw new IllegalArgumentException("Section not found");
        }

        activeWeekRepository.deleteBySectionId(sectionId);

        for (ActiveWeek week : weeks) {
            week.setSectionId(sectionId);
        }

        return activeWeekRepository.saveAll(weeks);
    }

    private List<ActiveWeek> generateWeeks(Section section) {
        List<ActiveWeek> weeks = new ArrayList<>();

        LocalDate start = section.getStartDate();
        while (start.getDayOfWeek().getValue() != 1) {
            start = start.minusDays(1);
        }

        LocalDate sectionEnd = section.getEndDate();

        while (!start.isAfter(sectionEnd)) {
            ActiveWeek week = new ActiveWeek();
            week.setSectionId(section.getId());
            week.setStartDate(start);
            week.setEndDate(start.plusDays(6));
            week.setActive(true);
            weeks.add(week);

            start = start.plusWeeks(1);
        }

        return weeks;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }
}
