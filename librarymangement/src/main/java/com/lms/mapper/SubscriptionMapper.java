package com.lms.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lms.Model.Subscription;
import com.lms.Model.SubscriptionPlan;
import com.lms.Model.User;
import com.lms.exception.SubscriptionException;
import com.lms.payload.dto.SubscriptionDto;
import com.lms.repository.SubscriptionPlanRepository;
import com.lms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SubscriptionMapper {

    private final UserRepository userRepository;

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionDto toDto(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(subscription.getId());


        if (subscription.getUser() !=  null) {
            dto.setUserId(subscription.getUser().getId());
            dto.setUserName(subscription.getUser().getFullName());
            dto.setUserEmail(subscription.getUser().getEmail());
        }

        if (subscription.getPlan() != null) {
            dto.setPlanId(subscription.getPlan().getId());
        }

        dto.setPlanName(subscription.getPlanName());
        dto.setPlanCode(subscription.getPlanCode());
        dto.setPrice(subscription.getPrice());
        dto.setStarDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setIsActive(subscription.getIsActive());
        dto.setMaxBooksAllowed(subscription.getMaxBooksAllowed());
        dto.setMaxDaysPerBooks(subscription.getMaxBooksAllowed());
        dto.setAutoRenew(subscription.getAutoRenew());
        dto.setCancelledAt(subscription.getCancelledAt());
        dto.setCancellationReason(subscription.getCancellationReason());
        dto.setNotes(subscription.getNotes());
        dto.setCreatedAt(subscription.getCreatedAt());
        dto.setUpdatedAt(subscription.getUpdatedAt());

        dto.setDaysRemaining(subscription.getDaysRemaining());
        dto.setIsValid(subscription.isValid());
        dto.setIsExpired(subscription.isExpired());
        return dto;
    }

    public Subscription toEntity(SubscriptionDto dto) throws SubscriptionException {
        if (dto == null) {
            return null;
        }

        Subscription subscription = new Subscription();
        subscription.setId(dto.getId());

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                            .orElseThrow(() -> new SubscriptionException("User not found with ID:" + dto.getUserId()));
            subscription.setUser(user);
        }

        if (dto.getPlanId() !=  null) {
            SubscriptionPlan plan = subscriptionPlanRepository.findById(dto.getPlanId())
                                        .orElseThrow(() -> new SubscriptionException("Subscription plan not found"));
            subscription.setPlan(plan);
        }

        subscription.setPlanName(dto.getPlanName());
        subscription.setPlanCode(dto.getPlanCode());
        subscription.setPrice(dto.getPrice());

        subscription.setStartDate(dto.getStarDate());
        subscription.setEndDate(dto.getEndDate());
        subscription.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        subscription.setMaxBooksAllowed(dto.getMaxBooksAllowed());
        subscription.setMaxDaysPerBook(dto.getMaxDaysPerBooks());
        subscription.setAutoRenew(dto.getAutoRenew() != null ? dto.getAutoRenew() : false);
        subscription.setCancelledAt(dto.getCancelledAt());
        subscription.setCancellationReason(dto.getCancellationReason());
        subscription.setNotes(dto.getNotes());

        return subscription;

    }

    public List<SubscriptionDto> toDtoList(List<Subscription> subscriptions) {
        if (subscriptions == null) {
            return null;
        }
        return subscriptions.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());   
    }
}
