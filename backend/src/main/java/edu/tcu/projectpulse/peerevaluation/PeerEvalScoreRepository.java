package edu.tcu.projectpulse.peerevaluation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeerEvalScoreRepository extends JpaRepository<PeerEvalScore, Integer> {

    List<PeerEvalScore> findByEvaluationId(Integer evaluationId);
}
