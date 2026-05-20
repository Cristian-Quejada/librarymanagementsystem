package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
