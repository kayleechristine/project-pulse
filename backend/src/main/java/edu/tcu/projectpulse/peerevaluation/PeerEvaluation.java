package edu.tcu.projectpulse.peerevaluation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "peer_evaluations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"evaluator_id", "evaluatee_id", "week_id"})
)
public class PeerEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "evaluator_id", nullable = false)
    private Integer evaluatorId;

    @Column(name = "evaluatee_id", nullable = false)
    private Integer evaluateeId;

    // Plain Integer until active_weeks migration lands
    @Column(name = "week_id")
    private Integer weekId;

    @Column(columnDefinition = "TEXT")
    private String publicComments;

    @Column(columnDefinition = "TEXT")
    private String privateComments;

    private Instant submittedAt;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PeerEvalScore> scores = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public Integer getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(Integer evaluatorId) {
        this.evaluatorId = evaluatorId;
    }

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

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public List<PeerEvalScore> getScores() {
        return scores;
    }

    public void setScores(List<PeerEvalScore> scores) {
        this.scores = scores;
    }
}
