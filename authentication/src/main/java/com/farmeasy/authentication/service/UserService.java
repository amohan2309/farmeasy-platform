package com.farmeasy.authentication.service;

import com.farmeasy.authentication.dto.*;
import com.farmeasy.authentication.entity.*;
import com.farmeasy.authentication.repository.*;
import com.farmeasy.authentication.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private PasswordUtil passwordUtil;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        String salt = passwordUtil.generateSalt();
        String hash = passwordUtil.hashPassword(dto.getPassword(), salt);
        User user = User.builder()
                .userId(UUID.randomUUID())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .passwordHash(hash)
                .salt(salt)
                .isActive(true)
                .isVerified(false)
                .mfaEnabled(false)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<Role> roles = dto.getRoles().stream()
                    .map(roleRepository::findByRoleName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        userRepository.save(user);
        return toResponseDTO(user);
    }

    public Optional<UserResponseDTO> getUserById(UUID userId) {
        return userRepository.findById(userId).map(this::toResponseDTO);
    }

    public Optional<UserResponseDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::toResponseDTO);
    }

    @Transactional
    public UserResponseDTO updateUser(UUID userId, UserRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow();
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getPassword() != null) {
            String salt = passwordUtil.generateSalt();
            String hash = passwordUtil.hashPassword(dto.getPassword(), salt);
            user.setPasswordHash(hash);
            user.setSalt(salt);
        }
        user.setUpdatedAt(java.time.LocalDateTime.now());
        userRepository.save(user);
        return toResponseDTO(user);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setActive(user.isActive());
        dto.setVerified(user.isVerified());
        dto.setMfaEnabled(user.isMfaEnabled());
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));
        }
        return dto;
    }
}