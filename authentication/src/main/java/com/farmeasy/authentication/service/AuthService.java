package com.farmeasy.authentication.service;

import com.farmeasy.authentication.dto.*;
import com.farmeasy.authentication.entity.*;
import com.farmeasy.authentication.repository.*;
import com.farmeasy.authentication.util.JwtUtil;
import com.farmeasy.authentication.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Optional<User> userOpt = userRepository.findByUsername(dto.getUsernameOrEmail());
        if (!userOpt.isPresent()) {
            userOpt = userRepository.findByEmail(dto.getUsernameOrEmail());
        }
        if (!userOpt.isPresent()) throw new RuntimeException("User not found");
        User user = userOpt.get();
        if (!passwordUtil.verifyPassword(dto.getPassword(), user.getPasswordHash(), user.getSalt())) {
            throw new RuntimeException("Invalid credentials");
        }
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        // Save refresh token
        AuthToken token = AuthToken.builder()
                .tokenId(java.util.UUID.randomUUID())
                .user(user)
                .token(refreshToken)
                .tokenType("refresh")
                .issuedAt(java.time.LocalDateTime.now())
                .expiresAt(java.time.LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();
        authTokenRepository.save(token);
        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserId(user.getUserId());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @Transactional
    public void logout(String refreshToken) {
        AuthToken token = authTokenRepository.findAll().stream()
                .filter(t -> t.getToken().equals(refreshToken) && !t.isRevoked())
                .findFirst().orElseThrow(() -> new RuntimeException("Token not found"));
        token.setRevoked(true);
        authTokenRepository.save(token);
    }

    public LoginResponseDTO refreshToken(String refreshToken) {
        AuthToken token = authTokenRepository.findAll().stream()
                .filter(t -> t.getToken().equals(refreshToken) && !t.isRevoked())
                .findFirst().orElseThrow(() -> new RuntimeException("Token not found or revoked"));
        User user = token.getUser();
        String accessToken = jwtUtil.generateAccessToken(user);
        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserId(user.getUserId());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }
}
