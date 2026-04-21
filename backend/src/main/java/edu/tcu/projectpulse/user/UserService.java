package edu.tcu.projectpulse.user;

import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByInvitationToken(String invitationToken) {
        return userRepository.findByInvitationToken(invitationToken);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
