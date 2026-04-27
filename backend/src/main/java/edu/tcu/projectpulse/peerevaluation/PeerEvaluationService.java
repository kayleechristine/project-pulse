package edu.tcu.projectpulse.peerevaluation;

import edu.tcu.projectpulse.peerevaluation.dto.PeerEvaluationRequest;
import edu.tcu.projectpulse.peerevaluation.dto.ScoreEntry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class PeerEvaluationService {

    private final PeerEvaluationRepository evalRepository;

    public PeerEvaluationService(PeerEvaluationRepository evalRepository) {
        this.evalRepository = evalRepository;
    }

    @Transactional
    public PeerEvaluation submit(Integer evaluatorId, PeerEvaluationRequest request) {
        // TODO: validate evaluatee is a teammate (needs TeamMemberRepository — Leiton)
        // TODO: validate weekId is the previous active week (needs ActiveWeekRepository — Leiton)

        PeerEvaluation eval = evalRepository
                .findByEvaluatorIdAndEvaluateeIdAndWeekId(
                        evaluatorId, request.getEvaluateeId(), request.getWeekId())
                .orElse(new PeerEvaluation());

        eval.setEvaluatorId(evaluatorId);
        eval.setEvaluateeId(request.getEvaluateeId());
        eval.setWeekId(request.getWeekId());
        eval.setPublicComments(request.getPublicComments());
        eval.setPrivateComments(request.getPrivateComments());
        eval.setSubmittedAt(Instant.now());

        eval.getScores().clear();
        for (ScoreEntry entry : request.getScores()) {
            PeerEvalScore score = new PeerEvalScore();
            score.setEvaluation(eval);
            score.setCriterionId(entry.getCriterionId());
            score.setScore(entry.getScore());
            eval.getScores().add(score);
        }

        return evalRepository.save(eval);
    }

    public List<PeerEvaluation> getEvaluationsReceived(Integer evaluateeId, Integer weekId) {
        return evalRepository.findByEvaluateeIdAndWeekId(evaluateeId, weekId);
    }

    public List<PeerEvaluation> getEvaluationsSubmitted(Integer evaluatorId) {
        return evalRepository.findByEvaluatorId(evaluatorId);
    }
}
