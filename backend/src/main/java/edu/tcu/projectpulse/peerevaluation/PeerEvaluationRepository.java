package edu.tcu.projectpulse.peerevaluation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PeerEvaluationRepository extends JpaRepository<PeerEvaluation, Integer> {

    List<PeerEvaluation> findByEvaluatorId(Integer evaluatorId);

    List<PeerEvaluation> findByEvaluateeIdAndWeekId(Integer evaluateeId, Integer weekId);

    Optional<PeerEvaluation> findByEvaluatorIdAndEvaluateeIdAndWeekId(Integer evaluatorId, Integer evaluateeId, Integer weekId);

    boolean existsByEvaluatorIdAndEvaluateeIdAndWeekId(Integer evaluatorId, Integer evaluateeId, Integer weekId);

    List<PeerEvaluation> findByEvaluatorIdAndWeekId(Integer evaluatorId, Integer weekId);

    List<PeerEvaluation> findByEvaluatorIdOrEvaluateeId(Integer evaluatorId, Integer evaluateeId);
}
