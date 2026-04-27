package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final WarReportService warReportService;

    public ReportController(WarReportService warReportService) {
        this.warReportService = warReportService;
    }

    @GetMapping("/war")
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
    public Result getTeamWarReport(@RequestParam Integer teamId,
                                   @RequestParam Integer weekId) {
        Map<String, Object> report = warReportService.buildTeamWarReport(teamId, weekId);
        return new Result(true, StatusCode.SUCCESS, "WAR report generated.", report);
    }
}
