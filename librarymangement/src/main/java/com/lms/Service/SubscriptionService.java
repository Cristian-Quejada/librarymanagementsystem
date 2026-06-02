package com.lms.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lms.exception.SubscriptionException;
import com.lms.payload.dto.SubscriptionDto;
import com.lms.payload.response.PaymentInitiateResponse;

public interface SubscriptionService {

    PaymentInitiateResponse subscribe(SubscriptionDto subscriptionDto) throws Exception;

    SubscriptionDto getUsersActiveSubscription(Long userId) throws SubscriptionException, Exception;

    SubscriptionDto cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException;

    SubscriptionDto activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException;

    List<SubscriptionDto> getAllSubscriptions(Pageable pageable);

    void deactivateExpiredSubscription() throws Exception;
}
