package com.lms.payload.dto;

import java.time.LocalDateTime;

import com.lms.domain.UserRole;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;
    private String phone;

    @NotNull(message = "Full name is required")
    private String fullName;
    private UserRole role;
    private String username;

    private LocalDateTime lastLogin;
}
