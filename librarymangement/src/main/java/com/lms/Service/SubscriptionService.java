package com.lms.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lms.payload.dto.SubscriptionDto;

public interface SubscriptionService {

    SubscriptionDto subscribe(SubscriptionDto subscriptionDto);

    SubscriptionDto getUsersActiveSubscription(Long userId);

    SubscriptionDto cancelSubscription(Long subscriptionId, String reason);

    SubscriptionDto activeSubscription(Long subscriptionId, Long paymentId);

    List<SubscriptionDto> getAllSubscriptions(Pageable pageable);
}
