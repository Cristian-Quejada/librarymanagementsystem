package com.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.Model.Payment;
import com.lms.Model.User;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

    Optional<User> findByGatewayOrderId(Object gatewayOrderId);


}
