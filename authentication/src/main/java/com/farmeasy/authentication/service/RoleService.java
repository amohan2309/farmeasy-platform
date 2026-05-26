package com.farmeasy.authentication.service;

import com.farmeasy.authentication.entity.Role;
import com.farmeasy.authentication.entity.User;
import com.farmeasy.authentication.repository.RoleRepository;
import com.farmeasy.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Transactional
    public void assignRoles(UUID userId, Set<String> roleNames) {
        User user = userRepository.findById(userId).orElseThrow();
        Set<Role> roles = roleNames.stream()
                .map(roleRepository::findByRoleName)
                .filter(r -> r != null)
                .collect(java.util.stream.Collectors.toSet());
        user.setRoles(roles);
        userRepository.save(user);
    }

    public Set<String> getUserRoles(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getRoles().stream().map(Role::getRoleName).collect(java.util.stream.Collectors.toSet());
    }

    public boolean userHasRole(UUID userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getRoles().stream().anyMatch(r -> r.getRoleName().equals(roleName));
    }
}
