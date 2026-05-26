package com.farmeasy.authentication.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserRequestDTO {
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private Set<String> roles;
}
