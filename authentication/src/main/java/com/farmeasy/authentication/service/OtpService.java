package com.farmeasy.authentication.service;

import com.farmeasy.authentication.entity.OtpVerification;
import com.farmeasy.authentication.entity.User;
import com.farmeasy.authentication.repository.OtpVerificationRepository;
import com.farmeasy.authentication.repository.UserRepository;
import com.farmeasy.authentication.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpVerificationRepository otpRepo;
    private final UserRepository userRepo;
    private final OtpUtil otpUtil;

    @Transactional
    public OtpVerification sendOtp(UUID userId, String deliveryMethod) {
        User user = userRepo.findById(userId).orElseThrow();
        String otpCode = otpUtil.generateOtp();
        OtpVerification otp = OtpVerification.builder()
                .otpId(UUID.randomUUID())
                .user(user)
                .otpCode(otpCode)
                .deliveryMethod(deliveryMethod)
                .isUsed(false)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();
        otpRepo.save(otp);
        // TODO: Integrate with SMS/Email provider
        return otp;
    }

    @Transactional
    public boolean verifyOtp(UUID userId, String otpCode) {
        OtpVerification otp = otpRepo.findAll().stream()
                .filter(o -> o.getUser().getUserId().equals(userId) && o.getOtpCode().equals(otpCode) && !o.isUsed() && o.getExpiresAt().isAfter(LocalDateTime.now()))
                .findFirst().orElse(null);
        if (otp == null) return false;
        otp.setUsed(true);
        otpRepo.save(otp);
        return true;
    }
}
