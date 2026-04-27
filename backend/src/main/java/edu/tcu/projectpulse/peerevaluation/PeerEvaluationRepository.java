package edu.tcu.projectpulse.peerevaluation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeerEvaluationRepository extends JpaRepository<PeerEvaluation, Integer> {

    List<PeerEvaluation> findByEvaluatorId(Integer evaluatorId);

    List<PeerEvaluation> findByEvaluateeIdAndWeekId(Integer evaluateeId, Integer weekId);

    boolean existsByEvaluatorIdAndEvaluateeIdAndWeekId(Integer evaluatorId, Integer evaluateeId, Integer weekId);
}
