package com.farmeasy.authentication.config;

import com.farmeasy.authentication.entity.User;
import com.farmeasy.authentication.repository.UserRepository;
import com.farmeasy.authentication.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthDataSeeder implements CommandLineRunner {
    private static final UUID DEMO_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final String DEMO_USERNAME = "demo";
    private static final String DEMO_PHONE = "9876543210";
    private static final String DEMO_PASSWORD = "FarmEasy123";

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.findByUsername(DEMO_USERNAME).isPresent()) {
            return;
        }

        var existing = userRepository.findByPhoneNumber(DEMO_PHONE);
        if (existing.isPresent()) {
            User user = existing.get();
            String salt = passwordUtil.generateSalt();
            user.setUsername(DEMO_USERNAME);
            user.setEmail("demo@farmeasy.local");
            user.setPasswordHash(passwordUtil.hashPassword(DEMO_PASSWORD, salt));
            user.setSalt(salt);
            user.setVerified(true);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.saveAndFlush(user);
            return;
        }

        String salt = passwordUtil.generateSalt();
        String hash = passwordUtil.hashPassword(DEMO_PASSWORD, salt);
        userRepository.save(User.builder()
                .userId(DEMO_USER_ID)
                .username(DEMO_USERNAME)
                .email("demo@farmeasy.local")
                .phoneNumber(DEMO_PHONE)
                .passwordHash(hash)
                .salt(salt)
                .isActive(true)
                .isVerified(true)
                .mfaEnabled(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }
}
