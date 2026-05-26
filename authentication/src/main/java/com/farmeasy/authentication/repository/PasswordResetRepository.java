package com.farmeasy.authentication.repository;

import com.farmeasy.authentication.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> {
}
