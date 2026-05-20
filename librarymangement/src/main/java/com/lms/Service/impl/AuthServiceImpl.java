package com.lms.Service.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.Model.User;
import com.lms.Service.AuthService;
import com.lms.configurations.JwtProvider;
import com.lms.domain.UserRole;
import com.lms.exception.UserException;
import com.lms.mapper.UserMapper;
import com.lms.payload.dto.UserDto;
import com.lms.payload.response.AuthResponse;
import com.lms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    @Override
    public AuthResponse login(String email, String password) throws UserException {
        Authentication authentication =  authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        //String role = authorities.iterator().next().getAuthority();
        String token = JwtProvider.generateToken(authentication);

        User user = UserRepository.findByEmail(email);

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

         AuthResponse response = new AuthResponse();
         response.setMessage("Login success");
         response.setTitle("Welcome back " + user.getFullName());
         response.setJwt(token);
         response.setUser(UserMapper.toUserDto(user));
         return response;
    }

    private Authentication authenticate(String username, String password) throws UserException {
        UserDetails userDetails = new CustomUserServiceImpl(userRepository).loadUserByUsername(username);

        if (userDetails == null) {
            throw new UserException("Username not found with email: " + username);
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UserException("Password not match");
        }
        return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
    }

    @Override
    public AuthResponse signUp(UserDto req) throws UserException {
        User user = UserRepository.findByEmail(req.getEmail());

        if (user == null) {

            throw new UserException("Email already registered!");
        }
        User createdUser = new User();
        createdUser.setEmail(req.getEmail());
        createdUser.setPassword(passwordEncoder.encode(req.getPassword()));
        createdUser.setPhone(req.getPhone());
        createdUser.setFullName(req.getFullName());
        createdUser.setLastLogin(LocalDateTime.now());
        createdUser.setRole(UserRole.ROLE_USER);

        User savedUser = userRepository.save(createdUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), savedUser.getPassword());

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setTitle("Welcome" + createdUser.getFullName());
        response.setMessage("Registration success");
        response.setUser(UserMapper.toUserDto(savedUser));
        return response;
    }

    @Override
    public void createPasswordResetToken(String email) throws UserException {
        User user = UserRepository.findByEmail(email);

        if (user == null) {
            throw new UserException("User not found with email: " + email);
        }

        String token = UUID.randomUUID().toString();
        
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetPassword'");
    }

}
