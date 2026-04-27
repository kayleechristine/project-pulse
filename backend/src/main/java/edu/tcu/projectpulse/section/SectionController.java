package edu.tcu.projectpulse.section;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sections")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class SectionController {

    private final SectionRepository sectionRepository;

    public SectionController(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @GetMapping
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Section getSection(@PathVariable Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Section createSection(@Valid @RequestBody SectionRequest request) {
        validateDates(request);

        if (sectionRepository.findBySectionNameIgnoreCase(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Section name already exists");
        }

        Section section = new Section(
                request.getName().trim(),
                request.getStartDate(),
                request.getEndDate(),
                request.getRubricId()
        );

        return sectionRepository.save(section);
    }

    @PutMapping("/{id}")
    public Section updateSection(@PathVariable Long id, @Valid @RequestBody SectionRequest request) {
        validateDates(request);

        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));

        sectionRepository.findBySectionNameIgnoreCase(request.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("Section name already exists");
            }
        });

        section.setName(request.getName().trim());
        section.setStartDate(request.getStartDate());
        section.setEndDate(request.getEndDate());
        section.setRubricId(request.getRubricId());

        return sectionRepository.save(section);
    }

    private void validateDates(SectionRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }
}