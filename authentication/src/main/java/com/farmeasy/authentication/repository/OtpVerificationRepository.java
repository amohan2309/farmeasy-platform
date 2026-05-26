package com.farmeasy.authentication.repository;

import com.farmeasy.authentication.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, UUID> {
}
