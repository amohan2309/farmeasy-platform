package com.farmeasy.authentication.service;

import com.farmeasy.authentication.entity.User;
import com.farmeasy.authentication.entity.UserDevice;
import com.farmeasy.authentication.repository.UserDeviceRepository;
import com.farmeasy.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final UserDeviceRepository deviceRepo;
    private final UserRepository userRepo;

    @Transactional
    public UserDevice registerDevice(UUID userId, String deviceName, String deviceType, String ipAddress, String userAgent) {
        User user = userRepo.findById(userId).orElseThrow();
        UserDevice device = UserDevice.builder()
                .deviceId(UUID.randomUUID())
                .user(user)
                .deviceName(deviceName)
                .deviceType(deviceType)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .lastLogin(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
        deviceRepo.save(device);
        return device;
    }

    @Transactional
    public void updateLastLogin(UUID deviceId) {
        UserDevice device = deviceRepo.findById(deviceId).orElseThrow();
        device.setLastLogin(LocalDateTime.now());
        deviceRepo.save(device);
    }
}
