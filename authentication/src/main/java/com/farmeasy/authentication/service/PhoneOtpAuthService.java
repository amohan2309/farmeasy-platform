package com.farmeasy.authentication.service;

import com.farmeasy.authentication.dto.LoginResponseDTO;
import com.farmeasy.authentication.entity.AuthToken;
import com.farmeasy.authentication.entity.User;
import com.farmeasy.authentication.repository.AuthTokenRepository;
import com.farmeasy.authentication.repository.UserRepository;
import com.farmeasy.authentication.util.JwtUtil;
import com.farmeasy.authentication.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhoneOtpAuthService {
    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;
    private final StringRedisTemplate redis;
    private final OtpUtil otpUtil;
    private final JwtUtil jwtUtil;

    @Value("${farmeasy.auth.dev-phone:9876543210}")
    private String devPhone;

    @Value("${farmeasy.auth.dev-otp:123456}")
    private String devOtp;

    public void sendOtp(String phoneNumber) {
        String otp = devPhone.equals(phoneNumber) ? devOtp : otpUtil.generateOtp();
        redis.opsForValue().set(otpKey(phoneNumber), otp, Duration.ofMinutes(5));
    }

    public LoginResponseDTO verifyAndLogin(String phoneNumber, String otpCode) {
        String expected = redis.opsForValue().get(otpKey(phoneNumber));
        if (expected == null || !expected.equals(otpCode)) {
            throw new RuntimeException("Invalid or expired OTP");
        }
        redis.delete(otpKey(phoneNumber));
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseGet(() -> createPhoneUser(phoneNumber));
        return issueTokens(user);
    }

    private User createPhoneUser(String phoneNumber) {
        User user = User.builder()
                .userId(UUID.randomUUID())
                .phoneNumber(phoneNumber)
                .username("farmer_" + phoneNumber.substring(Math.max(0, phoneNumber.length() - 4)))
                .isActive(true)
                .isVerified(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }

    private LoginResponseDTO issueTokens(User user) {
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        authTokenRepository.save(AuthToken.builder()
                .tokenId(UUID.randomUUID())
                .user(user)
                .token(refreshToken)
                .tokenType("refresh")
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build());
        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getUserId());
        return response;
    }

    private String otpKey(String phone) {
        return "otp:phone:" + phone;
    }
}
