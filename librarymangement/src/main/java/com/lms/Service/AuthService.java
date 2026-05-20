package com.lms.Service;

import org.springframework.stereotype.Service;

import com.lms.exception.UserException;
import com.lms.payload.dto.UserDto;
import com.lms.payload.response.AuthResponse;

@Service
public interface AuthService {

    AuthResponse login(String email, String password) throws UserException;
    AuthResponse signUp(UserDto req) throws UserException;

    void createPasswordResetToken(String email) throws UserException;
    void resetPassword(String token, String newPassword) throws Exception;
}
