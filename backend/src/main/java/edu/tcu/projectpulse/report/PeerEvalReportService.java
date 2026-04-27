package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.peerevaluation.PeerEvaluation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PeerEvalReportService {

    private final GradeCalculator gradeCalculator;

    public PeerEvalReportService(GradeCalculator gradeCalculator) {
        this.gradeCalculator = gradeCalculator;
    }

    public Map<String, Object> buildStudentReport(Integer evaluateeId, Integer weekId,
                                                   List<PeerEvaluation> evaluationsReceived) {
        double grade = gradeCalculator.calculate(evaluationsReceived);

        // Average score per criterion across all evaluators
        Map<Integer, Double> criterionAverages = evaluationsReceived.stream()
                .flatMap(eval -> eval.getScores().stream())
                .collect(Collectors.groupingBy(
                        score -> score.getCriterionId(),
                        Collectors.averagingInt(score -> score.getScore())
                ));

        // Public comments only — students never see private comments or evaluator identities
        List<String> publicComments = evaluationsReceived.stream()
                .map(PeerEvaluation::getPublicComments)
                .filter(c -> c != null && !c.isBlank())
                .collect(Collectors.toList());

        return Map.of(
                "evaluateeId", evaluateeId,
                "weekId", weekId,
                "grade", grade,
                "criterionAverages", criterionAverages,
                "publicComments", publicComments
        );
    }
}
