package edu.tcu.projectpulse.peerevaluation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "peer_evaluation_items",
    uniqueConstraints = @UniqueConstraint(columnNames = {"evaluation_id", "criterion_id"})
)
public class PeerEvalScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private PeerEvaluation evaluation;

    // Plain Integer until rubric_criteria migration lands
    @Column(name = "criterion_id", nullable = false)
    private Integer criterionId;

    @Column(nullable = false)
    private Integer score;

    public Integer getId() {
        return id;
    }

    public PeerEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(PeerEvaluation evaluation) {
        this.evaluation = evaluation;
    }

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
