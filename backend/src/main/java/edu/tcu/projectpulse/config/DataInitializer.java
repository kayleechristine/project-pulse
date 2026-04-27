package edu.tcu.projectpulse.config;

import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRepository;
import edu.tcu.projectpulse.user.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
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

        if (userRepository.findByEmail("student@test.com").isEmpty()) {
            User student = new User();
            student.setEmail("student@test.com");
            student.setFirstName("Jimothy");
            student.setLastName("Jimmerson");
            student.setPassword(passwordEncoder.encode("password123"));
            student.setEnabled(true);
            student.setAccountStatus(User.AccountStatus.ACTIVE);
            student.addRole(UserRole.STUDENT);
            userRepository.save(student);
        }
    }
}
