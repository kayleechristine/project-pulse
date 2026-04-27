package edu.tcu.projectpulse.section;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sectionName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private Long rubricId;

    public Section() {
    }

    public Section(String sectionName, LocalDate startDate, LocalDate endDate, Long rubricId) {
        this.sectionName = sectionName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rubricId = rubricId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return sectionName;
    }

    public void setName(String name) {
        this.sectionName = name;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getRubricId() {
        return rubricId;
    }

    public void setRubricId(Long rubricId) {
        this.rubricId = rubricId;
    }
}