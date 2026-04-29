package edu.tcu.projectpulse.war;

import edu.tcu.projectpulse.activeweek.ActiveWeek;
import edu.tcu.projectpulse.activeweek.ActiveWeekRepository;
import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.exception.ValidationException;
import edu.tcu.projectpulse.war.dto.WarActivityRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class WarActivityService {

    private final WarActivityRepository warActivityRepository;
    private final ActiveWeekRepository activeWeekRepository;

    public WarActivityService(WarActivityRepository warActivityRepository,
                               ActiveWeekRepository activeWeekRepository) {
        this.warActivityRepository = warActivityRepository;
        this.activeWeekRepository = activeWeekRepository;
    }

    public List<WarActivity> getActivities(Integer studentId, Integer weekId) {
        return warActivityRepository.findByStudentIdAndWeekId(studentId, weekId);
    }

    @Transactional
    public WarActivity add(Integer studentId, WarActivityRequest request) {
        ActiveWeek week = activeWeekRepository.findById(request.getWeekId().longValue())
                .orElseThrow(() -> new ResourceNotFoundException("ActiveWeek", "id", request.getWeekId()));

        if (!week.isActive()) {
            throw new ValidationException("Week " + request.getWeekId() + " is not an active week");
        }
        if (week.getStartDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Cannot submit activities for a future week");
        }

        WarActivity activity = new WarActivity();
        activity.setStudentId(studentId);
        mapRequest(activity, request);
        return warActivityRepository.save(activity);
    }

    @Transactional
    public WarActivity edit(Integer activityId, Integer studentId, WarActivityRequest request) {
        WarActivity activity = warActivityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("WarActivity", "id", activityId));

        if (!activity.getStudentId().equals(studentId)) {
            throw new ResourceNotFoundException("WarActivity", "id", activityId);
        }

        mapRequest(activity, request);
        return warActivityRepository.save(activity);
    }

    @Transactional
    public void delete(Integer activityId, Integer studentId) {
        WarActivity activity = warActivityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("WarActivity", "id", activityId));

        if (!activity.getStudentId().equals(studentId)) {
            throw new ResourceNotFoundException("WarActivity", "id", activityId);
        }

        warActivityRepository.delete(activity);
    }

    private void mapRequest(WarActivity activity, WarActivityRequest request) {
        activity.setWeekId(request.getWeekId());
        activity.setCategory(request.getCategory());
        activity.setDescription(request.getDescription());
        activity.setPlannedHours(request.getPlannedHours());
        activity.setActualHours(request.getActualHours());
        activity.setStatus(request.getStatus());
    }
}
