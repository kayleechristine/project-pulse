package edu.tcu.projectpulse.report;

import edu.tcu.projectpulse.activeweek.ActiveWeek;
import edu.tcu.projectpulse.activeweek.ActiveWeekRepository;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.war.WarActivity;
import edu.tcu.projectpulse.war.WarActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StudentWarReportServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ActiveWeekRepository activeWeekRepository;

    @Mock
    private WarActivityRepository warActivityRepository;

    @InjectMocks
    private StudentWarReportService service;

    @Test
    void should_GroupWarActivitiesByActiveWeekInDateRange() {
        User student = new User();
        student.setId(4);

        Team team = new Team();
        team.setSectionId(30L);

        ActiveWeek week = new ActiveWeek();
        ReflectionTestUtils.setField(week, "id", 12L);
        week.setStartDate(LocalDate.of(2026, 2, 2));
        week.setEndDate(LocalDate.of(2026, 2, 8));

        WarActivity activity = new WarActivity();
        activity.setWeekId(12);
        activity.setPlannedHours(3.0);
        activity.setActualHours(4.0);

        given(userRepository.findById(4)).willReturn(Optional.of(student));
        given(teamRepository.findByStudentId(4)).willReturn(Optional.of(team));
        given(activeWeekRepository.findBySectionIdAndEndDateGreaterThanEqualAndStartDateLessThanEqualOrderByStartDateAsc(
                30L, LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28))).willReturn(List.of(week));
        given(warActivityRepository.findByStudentIdAndWeekIdIn(4, List.of(12))).willReturn(List.of(activity));

        Map<String, Object> report = service.generate(4, LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> weeks = (List<Map<String, Object>>) report.get("weeks");
        assertThat(weeks).hasSize(1);
        assertThat(weeks.get(0).get("submitted")).isEqualTo(true);
        assertThat(weeks.get(0).get("plannedHours")).isEqualTo(3.0);
        assertThat(weeks.get(0).get("actualHours")).isEqualTo(4.0);
    }
}
