package com.lms.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lms.Model.User;
import com.lms.payload.dto.UserDto;

@Service
public class UserMapper {

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setPhone(user.getPhone());
        userDto.setLastLogin(user.getLastLogin());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public static List<UserDto> toDtoList(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public static Set<UserDto> toDtoList(Set<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toSet());
    }

    public static User toEntity(UserDto userDto) {
        User createdUser = new User();
        createdUser.setEmail(userDto.getEmail());
        createdUser.setPassword(userDto.getPassword());
        createdUser.setCreatedAt(LocalDateTime.now());
        createdUser.setPhone(userDto.getPhone());
        createdUser.setFullName(userDto.getFullName());
        createdUser.setRole(userDto.getRole());
        
        return createdUser;
    }
}
