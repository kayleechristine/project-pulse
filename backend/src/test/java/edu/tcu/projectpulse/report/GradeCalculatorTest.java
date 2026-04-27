package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.peerevaluation.PeerEvalScore;
import edu.tcu.projectpulse.peerevaluation.PeerEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GradeCalculatorTest {

    private GradeCalculator gradeCalculator;

    @BeforeEach
    void setUp() {
        gradeCalculator = new GradeCalculator();
    }

    private PeerEvaluation evalWithScores(int... scores) {
        PeerEvaluation eval = new PeerEvaluation();
        for (int s : scores) {
            PeerEvalScore score = new PeerEvalScore();
            score.setScore(s);
            score.setEvaluation(eval);
            eval.getScores().add(score);
        }
        return eval;
    }

    @Test
    void should_ReturnZero_When_NoEvaluationsReceived() {
        assertThat(gradeCalculator.calculate(List.of())).isEqualTo(0.0);
    }

    @Test
    void should_ReturnCorrectGrade_When_OneEvaluationReceived() {
        // Total: 10+9+10+9+10+10 = 58 → grade = 58/1 = 58
        PeerEvaluation eval = evalWithScores(10, 9, 10, 9, 10, 10);

        assertThat(gradeCalculator.calculate(List.of(eval))).isEqualTo(58.0);
    }

    @Test
    void should_ReturnAveragedGrade_When_MultipleEvaluationsReceived() {
        // From the spec example:
        // Tim gives John:  10+9+10+9+10+10 = 58
        // Lily gives John: 5+5+10+10+10+10 = 50
        // Grade = (58 + 50) / 2 = 54
        PeerEvaluation timEval  = evalWithScores(10, 9, 10, 9, 10, 10);
        PeerEvaluation lilyEval = evalWithScores(5, 5, 10, 10, 10, 10);

        assertThat(gradeCalculator.calculate(List.of(timEval, lilyEval))).isEqualTo(54.0);
    }

    @Test
    void should_ReturnCorrectGrade_When_AllScoresAreEqual() {
        PeerEvaluation eval1 = evalWithScores(10, 10, 10);
        PeerEvaluation eval2 = evalWithScores(10, 10, 10);

        assertThat(gradeCalculator.calculate(List.of(eval1, eval2))).isEqualTo(30.0);
    }
}
