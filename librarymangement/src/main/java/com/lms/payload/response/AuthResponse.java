package com.lms.payload.response;

import com.lms.payload.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String jwt;
    private String message;
    private String title;
    private UserDto user;
}
