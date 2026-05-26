package com.farmeasy.authentication.repository;

import com.farmeasy.authentication.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AuthTokenRepository extends JpaRepository<AuthToken, UUID> {
}
