package edu.tcu.projectpulse.rubric;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rubrics")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "https://orange-pebble-06afc810f.7.azurestaticapps.net"})
public class RubricController {

    private final RubricRepository rubricRepository;

    public RubricController(RubricRepository rubricRepository) {
        this.rubricRepository = rubricRepository;
    }

    @GetMapping
    public List<Rubric> getRubrics() {
        return rubricRepository.findAll();
    }

    @GetMapping("/{id}")
    public Rubric getRubric(@PathVariable Long id) {
        return rubricRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rubric not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rubric createRubric(@RequestBody Rubric rubric) {
        validateRubric(rubric);

        if (rubricRepository.findByNameIgnoreCase(rubric.getName()).isPresent()) {
            throw new IllegalArgumentException("Rubric name already exists");
        }

        rubric.setName(rubric.getName().trim());

        for (RubricCriterion criterion : rubric.getCriteria()) {
            criterion.setRubric(rubric);
        }

        return rubricRepository.save(rubric);
    }

    @PutMapping("/{id}")
    public Rubric updateRubric(@PathVariable Long id, @RequestBody Rubric request) {
        validateRubric(request);

        Rubric rubric = rubricRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rubric not found"));

        rubricRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("Rubric name already exists");
            }
        });

        rubric.setName(request.getName().trim());
        rubric.getCriteria().clear();

        for (RubricCriterion criterion : request.getCriteria()) {
            criterion.setRubric(rubric);
            rubric.getCriteria().add(criterion);
        }

        return rubricRepository.save(rubric);
    }

    private void validateRubric(Rubric rubric) {
        if (rubric.getName() == null || rubric.getName().isBlank()) {
            throw new IllegalArgumentException("Rubric name is required");
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
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }
}