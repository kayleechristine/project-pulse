package edu.tcu.projectpulse.activeweek;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "active_weeks")
public class ActiveWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sectionId;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean active;

    public Long getId() { return id; }

    public Long getSectionId() { return sectionId; }
    public void setSectionId(Long sectionId) { this.sectionId = sectionId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}