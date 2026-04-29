package edu.tcu.projectpulse.config;

import edu.tcu.projectpulse.activeweek.ActiveWeek;
import edu.tcu.projectpulse.activeweek.ActiveWeekRepository;
import edu.tcu.projectpulse.rubric.Rubric;
import edu.tcu.projectpulse.rubric.RubricCriterion;
import edu.tcu.projectpulse.rubric.RubricRepository;
import edu.tcu.projectpulse.section.Section;
import edu.tcu.projectpulse.section.SectionRepository;
import edu.tcu.projectpulse.team.Team;
import edu.tcu.projectpulse.team.TeamRepository;
import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.user.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@Profile("!prod")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SectionRepository sectionRepository;
    private final TeamRepository teamRepository;
    private final ActiveWeekRepository activeWeekRepository;
    private final RubricRepository rubricRepository;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           SectionRepository sectionRepository,
                           TeamRepository teamRepository,
                           ActiveWeekRepository activeWeekRepository,
                           RubricRepository rubricRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sectionRepository = sectionRepository;
        this.teamRepository = teamRepository;
        this.activeWeekRepository = activeWeekRepository;
        this.rubricRepository = rubricRepository;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        Section section = seedSection();
        seedActiveWeek(section);
        seedRubric(section);
        seedTeam(section);
    }

    private void seedUsers() {
        if (userRepository.findByEmail("admin@test.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@test.com");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setPassword(passwordEncoder.encode("password123"));
            admin.setEnabled(true);
            admin.setAccountStatus(User.AccountStatus.ACTIVE);
            admin.addRole(UserRole.ADMIN);
            userRepository.save(admin);
        }

        seedStudent("jimbo@tcu.edu", "Jimothy", "Jimmerson");
        seedStudent("bobinator@tcu.edu", "Bobert", "McBob");
        seedStudent("timtam@tcu.edu", "Timantha", "Tamothy");
    }

    private void seedStudent(String email, String firstName, String lastName) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User student = new User();
            student.setEmail(email);
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setPassword(passwordEncoder.encode("password123"));
            student.setEnabled(true);
            student.setAccountStatus(User.AccountStatus.ACTIVE);
            student.addRole(UserRole.STUDENT);
            userRepository.save(student);
        }
    }

    private Section seedSection() {
        return sectionRepository.findBySectionNameIgnoreCase("Section 001")
                .orElseGet(() -> {
                    Section section = new Section("Section 001",
                            LocalDate.of(2026, 1, 12),
                            LocalDate.of(2026, 5, 9),
                            null);
                    return sectionRepository.save(section);
                });
    }

    private void seedActiveWeek(Section section) {
        List<ActiveWeek> existing = activeWeekRepository.findBySectionIdOrderByStartDateAsc(section.getId());
        if (existing.isEmpty()) {
            ActiveWeek week = new ActiveWeek();
            week.setSectionId(section.getId());
            week.setStartDate(LocalDate.of(2026, 4, 27));
            week.setEndDate(LocalDate.of(2026, 5, 3));
            week.setActive(true);
            activeWeekRepository.save(week);
        }
    }

    private void seedRubric(Section section) {
        if (rubricRepository.findByNameIgnoreCase("Peer Evaluation Rubric").isEmpty()) {
            Rubric rubric = new Rubric();
            rubric.setName("Peer Evaluation Rubric");
            rubric.setCriteria(List.of(
                    criterion("Contribution to Team Goals", 10.0),
                    criterion("Quality of Work", 10.0),
                    criterion("Communication", 10.0),
                    criterion("Reliability / Dependability", 10.0),
                    criterion("Collaboration / Teamwork", 10.0),
                    criterion("Initiative / Problem Solving", 10.0)
            ));
            Rubric saved = rubricRepository.save(rubric);
            section.setRubricId(saved.getId());
            sectionRepository.save(section);
        }
    }

    private RubricCriterion criterion(String name, double maxScore) {
        RubricCriterion c = new RubricCriterion();
        c.setName(name);
        c.setDescription(name);
        c.setMaxScore(maxScore);
        return c;
    }

    private void seedTeam(Section section) {
        if (teamRepository.findByNameIgnoreCase("Team One").isEmpty()) {
            Integer jimothy = userRepository.findByEmail("jimbo@tcu.edu").map(User::getId).orElse(null);
            Integer bobert  = userRepository.findByEmail("bobinator@tcu.edu").map(User::getId).orElse(null);
            Integer timantha = userRepository.findByEmail("timtam@tcu.edu").map(User::getId).orElse(null);

            Team team = new Team();
            team.setName("Team One");
            team.setSectionId(section.getId());
            team.setStudentIds(Set.of(jimothy, bobert, timantha));
            teamRepository.save(team);
        }
    }
}
