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
import edu.tcu.projectpulse.war.ActivityCategory;
import edu.tcu.projectpulse.war.ActivityStatus;
import edu.tcu.projectpulse.war.WarActivity;
import edu.tcu.projectpulse.war.WarActivityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final WarActivityRepository warActivityRepository;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           SectionRepository sectionRepository,
                           TeamRepository teamRepository,
                           ActiveWeekRepository activeWeekRepository,
                           RubricRepository rubricRepository,
                           WarActivityRepository warActivityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sectionRepository = sectionRepository;
        this.teamRepository = teamRepository;
        this.activeWeekRepository = activeWeekRepository;
        this.rubricRepository = rubricRepository;
        this.warActivityRepository = warActivityRepository;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        Section section = seedSection();
        List<ActiveWeek> weeks = seedActiveWeeks(section);
        seedRubric(section);
        seedTeam(section);
        seedWarActivities(weeks);
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

        if (userRepository.findByEmail("jimbo@tcu.edu").isEmpty()) {
            User student = new User();
            student.setEmail("jimbo@tcu.edu");
            student.setFirstName("Jimbo");
            student.setLastName("Student");
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

    private List<ActiveWeek> seedActiveWeeks(Section section) {
        List<ActiveWeek> existing = activeWeekRepository.findBySectionIdOrderByStartDateAsc(section.getId());
        if (!existing.isEmpty()) return existing;

        LocalDate[][] ranges = {
            { LocalDate.of(2026, 3, 30), LocalDate.of(2026, 4, 5)  },
            { LocalDate.of(2026, 4, 6),  LocalDate.of(2026, 4, 12) },
            { LocalDate.of(2026, 4, 13), LocalDate.of(2026, 4, 19) },
            { LocalDate.of(2026, 4, 20), LocalDate.of(2026, 4, 26) },
            { LocalDate.of(2026, 4, 27), LocalDate.of(2026, 5, 3)  },
        };

        List<ActiveWeek> weeks = new ArrayList<>();
        for (LocalDate[] r : ranges) {
            ActiveWeek week = new ActiveWeek();
            week.setSectionId(section.getId());
            week.setStartDate(r[0]);
            week.setEndDate(r[1]);
            week.setActive(true);
            weeks.add(activeWeekRepository.save(week));
        }
        return weeks;
    }

    private void seedWarActivities(List<ActiveWeek> weeks) {
        if (warActivityRepository.count() > 0 || weeks.size() < 5) return;

        Integer jimothy  = userRepository.findByEmail("jimbo@tcu.edu").map(User::getId).orElse(null);
        Integer bobert   = userRepository.findByEmail("bobinator@tcu.edu").map(User::getId).orElse(null);
        Integer timantha = userRepository.findByEmail("timtam@tcu.edu").map(User::getId).orElse(null);
        if (jimothy == null || bobert == null || timantha == null) return;

        int w1 = weeks.get(0).getId().intValue();
        int w2 = weeks.get(1).getId().intValue();
        int w3 = weeks.get(2).getId().intValue();
        int w4 = weeks.get(3).getId().intValue();
        int w5 = weeks.get(4).getId().intValue();

        warActivityRepository.saveAll(List.of(
            // Week 1
            war(jimothy,  w1, ActivityCategory.PLANNING,       "Sprint 1 planning and backlog grooming",                     2.0, 2.0,  ActivityStatus.DONE),
            war(jimothy,  w1, ActivityCategory.DEVELOPMENT,    "Set up Spring Boot project structure and initial config",     6.0, 7.0,  ActivityStatus.DONE),
            war(bobert,   w1, ActivityCategory.PLANNING,       "Sprint 1 kickoff and task assignment",                       2.0, 2.0,  ActivityStatus.DONE),
            war(bobert,   w1, ActivityCategory.DESIGN,         "Wireframes for login and dashboard screens",                 5.0, 6.0,  ActivityStatus.DONE),
            war(timantha, w1, ActivityCategory.PLANNING,       "Sprint 1 planning and user story refinement",                2.0, 2.0,  ActivityStatus.DONE),
            war(timantha, w1, ActivityCategory.DOCUMENTATION,  "Draft requirements document and API spec",                   4.0, 5.0,  ActivityStatus.DONE),
            // Week 2
            war(jimothy,  w2, ActivityCategory.DEVELOPMENT,    "Implement JWT authentication and user registration",         8.0, 9.0,  ActivityStatus.DONE),
            war(jimothy,  w2, ActivityCategory.TESTING,        "Unit tests for auth service",                                3.0, 3.0,  ActivityStatus.DONE),
            war(bobert,   w2, ActivityCategory.DEVELOPMENT,    "Build login and registration Vue components",                6.0, 7.0,  ActivityStatus.DONE),
            war(bobert,   w2, ActivityCategory.COMMUNICATION,  "Daily standups and code review sessions",                   2.0, 2.0,  ActivityStatus.DONE),
            war(timantha, w2, ActivityCategory.DEVELOPMENT,    "Design and implement database schema",                       5.0, 6.0,  ActivityStatus.DONE),
            war(timantha, w2, ActivityCategory.DOCUMENTATION,  "Write API documentation for auth endpoints",                 3.0, 3.0,  ActivityStatus.DONE),
            // Week 3
            war(jimothy,  w3, ActivityCategory.DEVELOPMENT,    "Implement section and team management endpoints",            7.0, 8.0,  ActivityStatus.DONE),
            war(jimothy,  w3, ActivityCategory.BUGFIX,         "Fix CORS configuration for frontend integration",            2.0, 3.0,  ActivityStatus.DONE),
            war(bobert,   w3, ActivityCategory.DEVELOPMENT,    "Build admin dashboard and navigation components",            8.0, 8.0,  ActivityStatus.DONE),
            war(bobert,   w3, ActivityCategory.TESTING,        "Integration tests for frontend API calls",                   3.0, 4.0,  ActivityStatus.DONE),
            war(timantha, w3, ActivityCategory.DEVELOPMENT,    "Implement peer evaluation service and rubric management",    6.0, 7.0,  ActivityStatus.DONE),
            war(timantha, w3, ActivityCategory.PLANNING,       "Sprint 3 review and retrospective",                          2.0, 2.0,  ActivityStatus.DONE),
            // Week 4
            war(jimothy,  w4, ActivityCategory.DEVELOPMENT,    "Implement WAR activity CRUD endpoints",                     8.0, 9.0,  ActivityStatus.DONE),
            war(jimothy,  w4, ActivityCategory.TESTING,        "Unit tests for WAR activity service",                       3.0, 3.0,  ActivityStatus.DONE),
            war(bobert,   w4, ActivityCategory.DEVELOPMENT,    "Build WAR activity form and table components",               7.0, 8.0,  ActivityStatus.DONE),
            war(bobert,   w4, ActivityCategory.BUGFIX,         "Fix week selector not loading on page refresh",              2.0, 2.5,  ActivityStatus.DONE),
            war(timantha, w4, ActivityCategory.DEVELOPMENT,    "Implement WAR report service with non-submission flagging",  6.0, 6.0,  ActivityStatus.DONE),
            war(timantha, w4, ActivityCategory.COMMUNICATION,  "Sprint 4 demo presentation to instructor",                  1.0, 1.0,  ActivityStatus.DONE),
            // Week 5 — Jimothy and Timantha submitted; Bobert has not
            war(jimothy,  w5, ActivityCategory.DEVELOPMENT,    "Add active week validation to activity submission",          4.0, 4.0,  ActivityStatus.DONE),
            war(jimothy,  w5, ActivityCategory.PLANNING,       "Sprint 5 planning and backlog refinement",                   1.0, 1.0,  ActivityStatus.DONE),
            war(timantha, w5, ActivityCategory.DEVELOPMENT,    "Build instructor WAR report view",                           5.0, null, ActivityStatus.IN_PROGRESS)
        ));
    }

    private WarActivity war(Integer studentId, int weekId, ActivityCategory category,
                            String description, double planned, Double actual, ActivityStatus status) {
        WarActivity a = new WarActivity();
        a.setStudentId(studentId);
        a.setWeekId(weekId);
        a.setCategory(category);
        a.setDescription(description);
        a.setPlannedHours(planned);
        a.setActualHours(actual);
        a.setStatus(status);
        return a;
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
