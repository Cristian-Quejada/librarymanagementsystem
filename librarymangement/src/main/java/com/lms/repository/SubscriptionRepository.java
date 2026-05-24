package com.lms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lms.Model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>{

    @Query("select s from Subscription s where s.user.id = :userId AND " + 
            "s.isActive = true AND " + 
            "s.startDate<=:today AND s.endDate>=:today")
    Optional<Subscription> findActiveSubscription(
            @Param("userId") Long id,
            @Param("today") LocalDate today
    );

    @Query("select s from Subscription s where s.isActive = true " +
            "AND s.endDate<:today")
    List<Subscription> findExpiredSubscriptions(
            @Param("today") LocalDate today
    );
}
