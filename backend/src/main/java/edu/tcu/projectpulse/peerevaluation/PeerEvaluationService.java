package edu.tcu.projectpulse.peerevaluation;

import edu.tcu.projectpulse.activeweek.ActiveWeek;
import edu.tcu.projectpulse.activeweek.ActiveWeekRepository;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.peerevaluation.dto.PeerEvaluationRequest;
import edu.tcu.projectpulse.peerevaluation.dto.ScoreEntry;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class PeerEvaluationService {

    private final PeerEvaluationRepository evalRepository;
    private final TeamRepository teamRepository;
    private final ActiveWeekRepository activeWeekRepository;

    public PeerEvaluationService(PeerEvaluationRepository evalRepository,
                                 TeamRepository teamRepository,
                                 ActiveWeekRepository activeWeekRepository) {
        this.evalRepository = evalRepository;
        this.teamRepository = teamRepository;
        this.activeWeekRepository = activeWeekRepository;
    }

    @Transactional
    public PeerEvaluation submit(Integer evaluatorId, PeerEvaluationRequest request) {
        if (evaluatorId.equals(request.getEvaluateeId())) {
            throw new ValidationException("You cannot evaluate yourself.");
        }

        Team team = teamRepository.findByStudentId(evaluatorId)
                .orElseThrow(() -> new ValidationException("You are not assigned to a team."));
        if (!team.getStudentIds().contains(request.getEvaluateeId())) {
            throw new ValidationException("You can only evaluate members of your own team.");
        }

        ActiveWeek week = activeWeekRepository.findById(Long.valueOf(request.getWeekId()))
                .orElseThrow(() -> new ValidationException("The specified week does not exist."));
        if (!week.isActive()) {
            throw new ValidationException("Peer evaluations can only be submitted for active weeks.");
        }

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
