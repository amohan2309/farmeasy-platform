package com.farmeasy.authentication.service;

import com.farmeasy.authentication.entity.PasswordReset;
import com.farmeasy.authentication.entity.User;
import com.farmeasy.authentication.repository.PasswordResetRepository;
import com.farmeasy.authentication.repository.UserRepository;
import com.farmeasy.authentication.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetRepository resetRepo;
    private final UserRepository userRepo;
    @Autowired
    private PasswordUtil passwordUtil;

    @Transactional
    public PasswordReset requestReset(String emailOrUsername) {
        User user = userRepo.findByUsername(emailOrUsername)
                .orElse(userRepo.findByEmail(emailOrUsername).orElseThrow());
        PasswordReset reset = PasswordReset.builder()
                .resetId(UUID.randomUUID())
                .user(user)
                .resetToken(UUID.randomUUID().toString())
                .requestedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .isUsed(false)
                .build();
        resetRepo.save(reset);
        // TODO: Send resetToken via email
        return reset;
    }

    @Transactional
    public boolean resetPassword(String resetToken, String newPassword) {
        PasswordReset reset = resetRepo.findAll().stream()
                .filter(r -> r.getResetToken().equals(resetToken) && !r.isUsed() && r.getExpiresAt().isAfter(LocalDateTime.now()))
                .findFirst().orElse(null);
        if (reset == null) return false;
        User user = reset.getUser();
        String salt = passwordUtil.generateSalt();
        String hash = passwordUtil.hashPassword(newPassword, salt);
        user.setPasswordHash(hash);
        user.setSalt(salt);
        reset.setUsed(true);
        resetRepo.save(reset);
        userRepo.save(user);
        return true;
    }
}