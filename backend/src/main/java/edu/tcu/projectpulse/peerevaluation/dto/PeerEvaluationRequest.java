package edu.tcu.projectpulse.peerevaluation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PeerEvaluationRequest {

    @NotNull(message = "Evaluatee ID is required.")
    private Integer evaluateeId;

    @NotNull(message = "Week ID is required.")
    private Integer weekId;

    private String publicComments;

    private String privateComments;

    @NotEmpty(message = "At least one score is required.")
    @Valid
    private List<ScoreEntry> scores;

    public Integer getEvaluateeId() {
        return evaluateeId;
    }

    public void setEvaluateeId(Integer evaluateeId) {
        this.evaluateeId = evaluateeId;
    }

    public Integer getWeekId() {
        return weekId;
    }

    public void setWeekId(Integer weekId) {
        this.weekId = weekId;
    }

    public String getPublicComments() {
        return publicComments;
    }

    public void setPublicComments(String publicComments) {
        this.publicComments = publicComments;
    }

    public String getPrivateComments() {
        return privateComments;
    }

    public void setPrivateComments(String privateComments) {
        this.privateComments = privateComments;
    }

    public List<ScoreEntry> getScores() {
        return scores;
    }

    public void setScores(List<ScoreEntry> scores) {
        this.scores = scores;
    }
}
