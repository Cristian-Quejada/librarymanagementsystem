package com.lms.event.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lms.Model.Payment;
import com.lms.Service.SubscriptionService;
import com.lms.exception.SubscriptionException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final SubscriptionService subscriptionService;

    @Async
    @EventListener
    @Transactional
    public void handlePaymentPaymentSuccess(Payment payment) throws SubscriptionException {

        switch (payment.getPaymentType()) {
            case FINE:
                break;
            case LOST_BOOK_PENALTY:
                break;
            case DAMAGED_BOOK_PENALTY:
                break;
        
            case MEMBERSHIP:
                subscriptionService.activateSubscription(payment.getSubscription().getId(), payment.getId());
                break;
            default:
                break;
        }
    }
}
