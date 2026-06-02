package com.lms.mapper;

import org.springframework.stereotype.Component;

import com.lms.Model.Payment;
import com.lms.payload.dto.PaymentDto;

@Component
public class PaymentMapper {

    public PaymentDto toDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());
        dto.setUserName(payment.getUser().getFullName());
        dto.setUserEmail(payment.getUser().getEmail());

        // if (payment.getBookLoan() != null) {
        //     dto.setBookLoanId(payment.getBookLoan().getId());
        // }

        if (payment.getSubscription() != null) {
            dto.setSubscriptionId(payment.getSubscription().getId());
        }
        dto.setPaymentType(payment.getPaymentType());
        dto.setPaymentStatus(payment.getStatus());
        dto.setGateway(payment.getGateway());
        dto.setAmount(payment.getAmount());
        dto.setTransactionId(payment.getTransactionId());
        dto.setGatewayPaymentId(payment.getGatewayPaymentId());
        dto.setGatewayOrderId(payment.getGatewayOrderId());
        dto.setGatewaySignature(payment.getGatewaySignature());
        dto.setDescription(payment.getDescription());
        dto.setFailureReason(payment.getFailureReason());
        dto.setInitiatedAt(payment.getInitiatedAt());
        dto.setCompletedAt(payment.getCompletedAt());

        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return null;
    }
}
