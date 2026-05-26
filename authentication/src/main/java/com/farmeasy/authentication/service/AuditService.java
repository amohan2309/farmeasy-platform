package com.farmeasy.authentication.service;

import com.farmeasy.authentication.entity.AuthAudit;
import com.farmeasy.authentication.entity.User;
import com.farmeasy.authentication.repository.AuthAuditRepository;
import com.farmeasy.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuthAuditRepository auditRepo;
    private final UserRepository userRepo;

    @Transactional
    public void logEvent(UUID userId, String eventType, String ipAddress, String userAgent) {
        User user = userRepo.findById(userId).orElse(null);
        AuthAudit audit = AuthAudit.builder()
                .auditId(UUID.randomUUID())
                .user(user)
                .eventType(eventType)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .eventTime(LocalDateTime.now())
                .build();
        auditRepo.save(audit);
    }
}
