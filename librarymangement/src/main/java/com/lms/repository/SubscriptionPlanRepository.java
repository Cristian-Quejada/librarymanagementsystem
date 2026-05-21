package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.Model.SubscriptionPlan;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    Boolean existsByPlanCode(String planCode);
}
