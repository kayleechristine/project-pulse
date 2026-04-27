package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.peerevaluation.PeerEvaluation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GradeCalculator {

    // Algorithm: for each evaluation received, sum its criterion scores to get a total.
    // The grade is the average of those totals across all evaluators.
    public double calculate(List<PeerEvaluation> evaluationsReceived) {
        if (evaluationsReceived.isEmpty()) {
            return 0.0;
        }

        double sumOfTotals = evaluationsReceived.stream()
                .mapToDouble(eval -> eval.getScores().stream()
                        .mapToInt(score -> score.getScore())
                        .sum())
                .sum();

        return sumOfTotals / evaluationsReceived.size();
    }
}
