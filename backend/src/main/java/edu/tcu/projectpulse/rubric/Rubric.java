package edu.tcu.projectpulse.rubric;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rubrics")
public class Rubric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "rubric", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RubricCriterion> criteria = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RubricCriterion> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<RubricCriterion> criteria) {
        this.criteria.clear();

        if (criteria != null) {
            for (RubricCriterion criterion : criteria) {
                criterion.setRubric(this);
                this.criteria.add(criterion);
            }
        }
    }
}