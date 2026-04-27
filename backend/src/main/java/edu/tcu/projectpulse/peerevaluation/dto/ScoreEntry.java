package edu.tcu.projectpulse.peerevaluation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ScoreEntry {

    @NotNull(message = "Criterion ID is required.")
    private Integer criterionId;

    @NotNull(message = "Score is required.")
    @Min(value = 1, message = "Score must be at least 1.")
    private Integer score;

    public Integer getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(Integer criterionId) {
        this.criterionId = criterionId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
