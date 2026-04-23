package rw.koperative.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rw.koperative.model.User;
import rw.koperative.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Fix admin password on startup
        fixPassword("admin", "admin123");
        // Fix treasurer password on startup
        fixPassword("treasurer", "password123");
        // Fix secretary password on startup
        fixPassword("secretary", "password123");
    }

    private void fixPassword(String username, String rawPassword) {
        userRepository.findByUsername(username).ifPresent(user -> {
            if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
                user.setPasswordHash(passwordEncoder.encode(rawPassword));
                userRepository.save(user);
                System.out.println("✅ Password fixed for user: " + username);
            }
        });
    }
}
