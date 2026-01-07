package ru.bsuedu.cad.sem2.lab5.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createUserIfNotExists("user", "user@example.com", "password", Role.USER);
        createUserIfNotExists("moderator", "moderator@example.com", "password", Role.MODERATOR);
        createUserIfNotExists("creator", "creator@example.com", "password", Role.TASK_CREATOR);
    }

    private void createUserIfNotExists(String name, String email, String password, Role role) {
        if (userRepository.findByName(name).isEmpty()) {
            User user = new User(name, email, passwordEncoder.encode(password), role);
            userRepository.save(user);
        }
    }
}
