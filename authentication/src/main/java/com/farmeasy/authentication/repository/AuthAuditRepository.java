package com.farmeasy.authentication.repository;

import com.farmeasy.authentication.entity.AuthAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AuthAuditRepository extends JpaRepository<AuthAudit, UUID> {
}
