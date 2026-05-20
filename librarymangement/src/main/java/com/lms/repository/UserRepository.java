package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{

    static User findByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }
}
