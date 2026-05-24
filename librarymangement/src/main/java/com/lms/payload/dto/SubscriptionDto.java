package com.lms.payload.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {

    private Long id;

    @NotNull(message = "UserId is mandatory")
    private Long userId;

    private String userName;

    private String userEmail;

    @NotNull(message = "Subscription plan id is mandatory")
    private Long planId;

    private String planName;

    private String planCode;

    private Long price;

    private String currency;

    private LocalDate starDate;

    private LocalDate endDate;

    private Boolean isActive;

    private Integer maxBooksAllowed;

    private Integer maxDaysPerBooks;

    private Boolean autoRenew;

    private LocalDateTime cancelledAt;

    private String cancellationReason;

    private String notes;

    private Long daysRemaining;

    private Boolean isValid;

    private Boolean isExpired;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
