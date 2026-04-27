package edu.tcu.projectpulse.rubric;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RubricRepository extends JpaRepository<Rubric, Long> {
    Optional<Rubric> findByNameIgnoreCase(String name);
}