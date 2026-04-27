package edu.tcu.projectpulse.rubric;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rubrics")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class RubricController {

    private final RubricRepository rubricRepository;

    public RubricController(RubricRepository rubricRepository) {
        this.rubricRepository = rubricRepository;
    }

    @GetMapping
    public List<Rubric> getRubrics() {
        return rubricRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rubric createRubric(@RequestBody Rubric rubric) {
        if (rubric.getName() == null || rubric.getName().isBlank()) {
            throw new IllegalArgumentException("Rubric name is required");
        }

        if (rubricRepository.findByNameIgnoreCase(rubric.getName()).isPresent()) {
            throw new IllegalArgumentException("Rubric name already exists");
        }

        if (rubric.getCriteria() == null || rubric.getCriteria().isEmpty()) {
            throw new IllegalArgumentException("At least one criterion is required");
        }

        for (RubricCriterion criterion : rubric.getCriteria()) {
            if (criterion.getName() == null || criterion.getName().isBlank()) {
                throw new IllegalArgumentException("Criterion name is required");
            }

            if (criterion.getDescription() == null || criterion.getDescription().isBlank()) {
                throw new IllegalArgumentException("Criterion description is required");
            }

            if (criterion.getMaxScore() == null || criterion.getMaxScore() <= 0) {
                throw new IllegalArgumentException("Criterion max score must be positive");
            }

            criterion.setRubric(rubric);
        }

        rubric.setName(rubric.getName().trim());
        return rubricRepository.save(rubric);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }
}