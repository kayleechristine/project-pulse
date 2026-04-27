package edu.tcu.projectpulse.war.dto;

import edu.tcu.projectpulse.war.ActivityCategory;
import edu.tcu.projectpulse.war.ActivityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WarActivityRequest {

    @NotNull
    private Integer weekId;

    @NotNull
    private ActivityCategory category;

    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Double plannedHours;

    private Double actualHours;

    @NotNull
    private ActivityStatus status;

    public Integer getWeekId() { return weekId; }
    public void setWeekId(Integer weekId) { this.weekId = weekId; }

    public ActivityCategory getCategory() { return category; }
    public void setCategory(ActivityCategory category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPlannedHours() { return plannedHours; }
    public void setPlannedHours(Double plannedHours) { this.plannedHours = plannedHours; }

    public Double getActualHours() { return actualHours; }
    public void setActualHours(Double actualHours) { this.actualHours = actualHours; }

    public ActivityStatus getStatus() { return status; }
    public void setStatus(ActivityStatus status) { this.status = status; }
}
