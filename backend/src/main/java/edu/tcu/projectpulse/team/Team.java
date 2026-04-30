package edu.tcu.projectpulse.team;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sectionId;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private String websiteUrl;

    @ElementCollection
    @CollectionTable(name = "team_students", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "student_id")
    private java.util.Set<Integer> studentIds = new java.util.HashSet<>();

    @ElementCollection
    @CollectionTable(name = "team_instructors", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "instructor_id")
    private java.util.Set<Integer> instructorIds = new java.util.HashSet<>();

    public java.util.Set<Integer> getStudentIds() {
        return studentIds;
    }
    
    public void setStudentIds(java.util.Set<Integer> studentIds) {
        this.studentIds = studentIds;
    }

    public java.util.Set<Integer> getInstructorIds() {
        return instructorIds;
    }

    public void setInstructorIds(java.util.Set<Integer> instructorIds) {
        this.instructorIds = instructorIds;
    }
    
    public Long getId() {
        return id;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
}
