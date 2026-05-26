package com.lms.Service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lms.Model.Subscription;
import com.lms.Model.SubscriptionPlan;
import com.lms.Model.User;
import com.lms.Service.SubscriptionService;
import com.lms.Service.UserService;
import com.lms.exception.SubscriptionException;
import com.lms.mapper.SubscriptionMapper;
import com.lms.payload.dto.SubscriptionDto;
import com.lms.repository.SubscriptionPlanRepository;
import com.lms.repository.SubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionMapper subscriptionMapper;

    private final UserService userService;

    @Override
    public SubscriptionDto subscribe(SubscriptionDto subscriptionDto) throws Exception {
        User user = userService.getCurrentUser();

        SubscriptionPlan plan = subscriptionPlanRepository.findById(subscriptionDto.getPlanId())
                                .orElseThrow(() -> new SubscriptionException("Subscription plan not found"));
        
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDto, plan, user);
        subscription.initializedFromPlan();
        subscription.setIsActive(false);
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toDto(savedSubscription);
    }

    @Override
    public SubscriptionDto getUsersActiveSubscription(Long userId) throws Exception {
        User user = userService.getCurrentUser();

        Subscription subscription = subscriptionRepository.findActiveSubscriptionByUserId(user.getId(), LocalDate.now())
                                    .orElseThrow(() -> new SubscriptionException("No Active subscription found!"));
        return subscriptionMapper.toDto(subscription);
    }

    @Override
    public SubscriptionDto cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                                    .orElseThrow(() -> new SubscriptionException("Subscription not found with ID: " + subscriptionId));
                    
        if (!subscription.getIsActive()) {
            throw new SubscriptionException("Subscription is already inactive");
        }

        subscription.setIsActive(false);
        subscription.setCancelledAt(LocalDateTime.now());
        subscription.setCancellationReason(reason != null ? reason: "Cancelled by user");

        subscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toDto(subscription);
    }

    @Override
    public SubscriptionDto activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                                    .orElseThrow(() -> new SubscriptionException("Subscription not found by id"));

        subscription.setIsActive(true);

        subscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDto(subscription);
    }

    @Override
    public List<SubscriptionDto> getAllSubscriptions(Pageable pageable) {

        List<Subscription> subscriptions = subscriptionRepository.findAll();

        return subscriptionMapper.toDtoList(subscriptions);
    }

    @Override
    public void deactivateExpiredSubscription() throws Exception {

        List<Subscription> expiredSubscriptions = subscriptionRepository.findExpiredSubscriptions(LocalDate.now());

        for (Subscription subscription : expiredSubscriptions) {
            subscription.setIsActive(false);
            subscriptionRepository.save(subscription);
        }
    }

}
