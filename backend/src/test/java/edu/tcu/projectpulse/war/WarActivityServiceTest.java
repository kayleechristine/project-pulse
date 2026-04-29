package edu.tcu.projectpulse.war;

import edu.tcu.projectpulse.activeweek.ActiveWeek;
import edu.tcu.projectpulse.activeweek.ActiveWeekRepository;
import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.war.dto.WarActivityRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WarActivityServiceTest {

    @Mock
    private WarActivityRepository warActivityRepository;

    @Mock
    private ActiveWeekRepository activeWeekRepository;

    @InjectMocks
    private WarActivityService warActivityService;

    private WarActivityRequest request;

    @BeforeEach
    void setUp() {
        request = new WarActivityRequest();
        request.setWeekId(1);
        request.setCategory(ActivityCategory.DEVELOPMENT);
        request.setDescription("Implement login feature");
        request.setPlannedHours(4.0);
        request.setActualHours(5.0);
        request.setStatus(ActivityStatus.DONE);
    }

    private ActiveWeek activeWeek(boolean active, LocalDate startDate) {
        ActiveWeek week = new ActiveWeek();
        week.setActive(active);
        week.setStartDate(startDate);
        return week;
    }

    @Test
    void should_AddActivity_When_WeekIsActiveAndNotFuture() {
        given(activeWeekRepository.findById(1L))
                .willReturn(Optional.of(activeWeek(true, LocalDate.now().minusDays(1))));
        given(warActivityRepository.save(any(WarActivity.class))).willAnswer(inv -> inv.getArgument(0));

        warActivityService.add(1, request);

        ArgumentCaptor<WarActivity> captor = ArgumentCaptor.forClass(WarActivity.class);
        verify(warActivityRepository).save(captor.capture());

        WarActivity saved = captor.getValue();
        assertThat(saved.getStudentId()).isEqualTo(1);
        assertThat(saved.getWeekId()).isEqualTo(1);
        assertThat(saved.getCategory()).isEqualTo(ActivityCategory.DEVELOPMENT);
        assertThat(saved.getDescription()).isEqualTo("Implement login feature");
        assertThat(saved.getPlannedHours()).isEqualTo(4.0);
        assertThat(saved.getActualHours()).isEqualTo(5.0);
        assertThat(saved.getStatus()).isEqualTo(ActivityStatus.DONE);
    }

    @Test
    void should_EditActivity_When_ActivityBelongsToStudent() {
        WarActivity existing = new WarActivity();
        existing.setStudentId(1);
        existing.setDescription("Old description");

        given(warActivityRepository.findById(42)).willReturn(Optional.of(existing));
        given(warActivityRepository.save(any(WarActivity.class))).willAnswer(inv -> inv.getArgument(0));

        warActivityService.edit(42, 1, request);

        ArgumentCaptor<WarActivity> captor = ArgumentCaptor.forClass(WarActivity.class);
        verify(warActivityRepository).save(captor.capture());

        assertThat(captor.getValue().getDescription()).isEqualTo("Implement login feature");
        assertThat(captor.getValue().getStatus()).isEqualTo(ActivityStatus.DONE);
    }

    @Test
    void should_ThrowException_When_EditingActivityOwnedByAnotherStudent() {
        WarActivity existing = new WarActivity();
        existing.setStudentId(99);

        given(warActivityRepository.findById(42)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> warActivityService.edit(42, 1, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void should_DeleteActivity_When_ActivityBelongsToStudent() {
        WarActivity existing = new WarActivity();
        existing.setStudentId(1);

        given(warActivityRepository.findById(42)).willReturn(Optional.of(existing));

        warActivityService.delete(42, 1);

        verify(warActivityRepository).delete(existing);
    }

    @Test
    void should_ThrowException_When_DeletingActivityOwnedByAnotherStudent() {
        WarActivity existing = new WarActivity();
        existing.setStudentId(99);

        given(warActivityRepository.findById(42)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> warActivityService.delete(42, 1))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void should_ReturnActivities_When_RequestedByStudentAndWeek() {
        WarActivity activity = new WarActivity();
        activity.setStudentId(1);
        activity.setWeekId(1);

        given(warActivityRepository.findByStudentIdAndWeekId(1, 1)).willReturn(List.of(activity));

        List<WarActivity> result = warActivityService.getActivities(1, 1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStudentId()).isEqualTo(1);
    }

    @Test
    void should_ThrowException_When_WeekIdNotFound() {
        given(activeWeekRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> warActivityService.add(1, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void should_ThrowException_When_WeekIsNotActive() {
        given(activeWeekRepository.findById(1L))
                .willReturn(Optional.of(activeWeek(false, LocalDate.now().minusDays(1))));

        assertThatThrownBy(() -> warActivityService.add(1, request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("not an active week");
    }

    @Test
    void should_ThrowException_When_WeekIsInFuture() {
        given(activeWeekRepository.findById(1L))
                .willReturn(Optional.of(activeWeek(true, LocalDate.now().plusDays(7))));

        assertThatThrownBy(() -> warActivityService.add(1, request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("future week");
    }
}
