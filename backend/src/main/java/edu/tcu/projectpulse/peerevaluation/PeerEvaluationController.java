package edu.tcu.projectpulse.peerevaluation;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.peerevaluation.dto.PeerEvaluationRequest;
import edu.tcu.projectpulse.report.PeerEvalReportService;
import edu.tcu.projectpulse.shared.Result;
import edu.tcu.projectpulse.shared.StatusCode;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/peer-evaluations")
public class PeerEvaluationController {

    private final PeerEvaluationService peerEvalService;
    private final PeerEvalReportService reportService;
    private final UserService userService;

    public PeerEvaluationController(PeerEvaluationService peerEvalService,
                                    PeerEvalReportService reportService,
                                    UserService userService) {
        this.peerEvalService = peerEvalService;
        this.reportService = reportService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public Result submit(@AuthenticationPrincipal UserDetails userDetails,
                         @RequestBody @Valid PeerEvaluationRequest request) {
        User evaluator = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userDetails.getUsername()));

        peerEvalService.submit(evaluator.getId(), request);
        return new Result(true, StatusCode.SUCCESS, "Peer evaluation submitted.", null);
    }

    @GetMapping("/report")
    @PreAuthorize("hasRole('STUDENT')")
    public Result getMyReport(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam Integer weekId) {
        User student = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userDetails.getUsername()));

        List<PeerEvaluation> received = peerEvalService.getEvaluationsReceived(student.getId(), weekId);
        Map<String, Object> report = reportService.buildStudentReport(student.getId(), weekId, received);
        return new Result(true, StatusCode.SUCCESS, "Report generated.", report);
    }
}
