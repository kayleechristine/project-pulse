package edu.tcu.projectpulse.war;

import edu.tcu.projectpulse.exception.ResourceNotFoundException;
import edu.tcu.projectpulse.war.dto.WarActivityRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarActivityService {

    private final WarActivityRepository warActivityRepository;

    public WarActivityService(WarActivityRepository warActivityRepository) {
        this.warActivityRepository = warActivityRepository;
    }

    public List<WarActivity> getActivities(Integer studentId, Integer weekId) {
        return warActivityRepository.findByStudentIdAndWeekId(studentId, weekId);
    }

    @Transactional
    public WarActivity add(Integer studentId, WarActivityRequest request) {
        // TODO: validate weekId is an active week and not in the future (needs ActiveWeekRepository)

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
