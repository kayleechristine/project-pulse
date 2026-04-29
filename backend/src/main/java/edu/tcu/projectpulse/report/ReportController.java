package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final WarReportService warReportService;
    private final SectionPeerEvalReportService sectionPeerEvalReportService;
    private final StudentPeerEvalReportService studentPeerEvalReportService;
    private final StudentWarReportService studentWarReportService;

    public ReportController(WarReportService warReportService,
                            SectionPeerEvalReportService sectionPeerEvalReportService,
                            StudentPeerEvalReportService studentPeerEvalReportService,
                            StudentWarReportService studentWarReportService) {
        this.warReportService = warReportService;
        this.sectionPeerEvalReportService = sectionPeerEvalReportService;
        this.studentPeerEvalReportService = studentPeerEvalReportService;
        this.studentWarReportService = studentWarReportService;
    }

    @GetMapping("/war")
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
    public Result getTeamWarReport(@RequestParam Integer teamId,
                                   @RequestParam Integer weekId) {
        Map<String, Object> report = warReportService.buildTeamWarReport(teamId, weekId);
        return new Result(true, StatusCode.SUCCESS, "WAR report generated.", report);
    }

    @GetMapping("/peer-evaluations/section")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public Result getSectionPeerEvaluationReport(@RequestParam Long sectionId,
                                                 @RequestParam Integer weekId) {
        Map<String, Object> report = sectionPeerEvalReportService.generate(sectionId, weekId);
        return new Result(true, StatusCode.SUCCESS, "Section peer evaluation report generated.", report);
    }

    @GetMapping("/peer-evaluations/student")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public Result getStudentPeerEvaluationReport(@RequestParam Integer studentId,
                                                 @RequestParam LocalDate startDate,
                                                 @RequestParam LocalDate endDate) {
        Map<String, Object> report = studentPeerEvalReportService.generate(studentId, startDate, endDate);
        return new Result(true, StatusCode.SUCCESS, "Student peer evaluation report generated.", report);
    }

    @GetMapping("/war/student")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public Result getStudentWarReport(@RequestParam Integer studentId,
                                      @RequestParam LocalDate startDate,
                                      @RequestParam LocalDate endDate) {
        Map<String, Object> report = studentWarReportService.generate(studentId, startDate, endDate);
        return new Result(true, StatusCode.SUCCESS, "Student WAR report generated.", report);
    }
}
