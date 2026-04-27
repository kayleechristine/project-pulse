package edu.tcu.projectpulse.peerevaluation;

import edu.tcu.projectpulse.peerevaluation.dto.PeerEvaluationRequest;
import edu.tcu.projectpulse.peerevaluation.dto.ScoreEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PeerEvaluationServiceTest {

    @Mock
    private PeerEvaluationRepository evalRepository;

    @InjectMocks
    private PeerEvaluationService peerEvalService;

    private PeerEvaluationRequest request;

    @BeforeEach
    void setUp() {
        ScoreEntry score1 = new ScoreEntry();
        score1.setCriterionId(1);
        score1.setScore(9);

        ScoreEntry score2 = new ScoreEntry();
        score2.setCriterionId(2);
        score2.setScore(8);

        request = new PeerEvaluationRequest();
        request.setEvaluateeId(2);
        request.setWeekId(1);
        request.setPublicComments("Great work.");
        request.setPrivateComments("Needs improvement in meetings.");
        request.setScores(List.of(score1, score2));
    }

    @Test
    void should_CreateNewEvaluation_When_NoneExistsForThatWeek() {
        given(evalRepository.findByEvaluatorIdAndEvaluateeIdAndWeekId(1, 2, 1))
                .willReturn(Optional.empty());
        given(evalRepository.save(any(PeerEvaluation.class))).willAnswer(inv -> inv.getArgument(0));

        peerEvalService.submit(1, request);

        ArgumentCaptor<PeerEvaluation> captor = ArgumentCaptor.forClass(PeerEvaluation.class);
        verify(evalRepository).save(captor.capture());

        PeerEvaluation saved = captor.getValue();
        assertThat(saved.getEvaluatorId()).isEqualTo(1);
        assertThat(saved.getEvaluateeId()).isEqualTo(2);
        assertThat(saved.getWeekId()).isEqualTo(1);
        assertThat(saved.getPublicComments()).isEqualTo("Great work.");
        assertThat(saved.getScores()).hasSize(2);
        assertThat(saved.getSubmittedAt()).isNotNull();
    }

    @Test
    void should_UpdateExistingEvaluation_When_OneAlreadyExists() {
        PeerEvaluation existing = new PeerEvaluation();
        existing.setEvaluatorId(1);
        existing.setEvaluateeId(2);
        existing.setWeekId(1);
        existing.setPublicComments("Old comment.");

        given(evalRepository.findByEvaluatorIdAndEvaluateeIdAndWeekId(1, 2, 1))
                .willReturn(Optional.of(existing));
        given(evalRepository.save(any(PeerEvaluation.class))).willAnswer(inv -> inv.getArgument(0));

        peerEvalService.submit(1, request);

        ArgumentCaptor<PeerEvaluation> captor = ArgumentCaptor.forClass(PeerEvaluation.class);
        verify(evalRepository).save(captor.capture());

        assertThat(captor.getValue().getPublicComments()).isEqualTo("Great work.");
        assertThat(captor.getValue().getScores()).hasSize(2);
    }

    @Test
    void should_ReturnEvaluationsReceived_When_RequestedByEvaluateeAndWeek() {
        PeerEvaluation eval = new PeerEvaluation();
        eval.setEvaluateeId(1);
        eval.setWeekId(1);

        given(evalRepository.findByEvaluateeIdAndWeekId(1, 1)).willReturn(List.of(eval));

        List<PeerEvaluation> result = peerEvalService.getEvaluationsReceived(1, 1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEvaluateeId()).isEqualTo(1);
    }
}
