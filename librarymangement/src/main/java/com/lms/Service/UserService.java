package com.lms.Service;

import java.util.List;
import java.util.stream.Stream;

import com.lms.Model.User;
import com.lms.payload.dto.UserDto;

public interface UserService {

    public User getCurrentUser() throws Exception;
    public List<UserDto> getAllUsers();
    User findById(Long id) throws Exception;
}
