package com.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Service.AuthService;
import com.lms.exception.UserException;
import com.lms.payload.dto.UserDto;
import com.lms.payload.request.ForgotPasswordRequest;
import com.lms.payload.request.LoginRequest;
import com.lms.payload.request.ResetPasswordRequest;
import com.lms.payload.response.ApiResponse;
import com.lms.payload.response.AuthResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUpHandler(@Valid @RequestBody UserDto req) throws UserException {
        AuthResponse response = authService.signUp(req);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@Valid @RequestBody LoginRequest req) throws UserException {
        AuthResponse response = authService.login(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPasswordRequest req) throws UserException{
        
        authService.createPasswordResetToken(req.getEmail());

        ApiResponse response = new ApiResponse("A reset link has been sent to you email.", true);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest req) throws Exception {

        authService.resetPassword(req.getToken(), req.getPassword());

        ApiResponse response = new ApiResponse("Password reset successfully", true);
        return ResponseEntity.ok(response);
    }
}
