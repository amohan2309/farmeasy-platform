package com.farmeasy.authentication.repository;

import com.farmeasy.authentication.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {
}
