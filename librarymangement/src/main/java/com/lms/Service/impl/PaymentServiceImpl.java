package com.lms.Service.impl;

import com.lms.mapper.PaymentMapper;
import java.time.LocalDateTime;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lms.Model.Payment;
import com.lms.Model.Subscription;
import com.lms.Model.User;
import com.lms.Service.PaymentService;
import com.lms.Service.gateway.RazorpayService;
import com.lms.domain.PaymentGateway;
import com.lms.domain.PaymentStatus;
import com.lms.event.publisher.PaymentEventPublisher;
import com.lms.payload.dto.PaymentDto;
import com.lms.payload.request.PaymentInitiateRequest;
import com.lms.payload.request.PaymentVerifyRequest;
import com.lms.payload.response.PaymentInitiateResponse;
import com.lms.payload.response.PaymentLinkResponse;
import com.lms.repository.PaymentRepository;
import com.lms.repository.SubscriptionRepository;
import com.lms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;

    private final UserRepository userRepository;

    private final SubscriptionRepository subscriptionRepository;

    private final PaymentRepository paymentRepository;

    private final RazorpayService razorpayService;

    private final PaymentEventPublisher paymentEventPublisher;

    

    @Override
    public PaymentInitiateResponse initiatePayment(PaymentInitiateRequest req) throws Exception {

        User user = userRepository.findById(req.getUserId()).get();

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentType(req.getPaymentType());
        payment.setGateway(req.getGateway());
        payment.setAmount(req.getAmount());
        payment.setDescription(req.getDescription());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId("" + UUID.randomUUID());
        payment.setInitiatedAt(LocalDateTime.now());

        if (req.getSubscriptionId() !=  null) {
            Subscription sub = subscriptionRepository.findById(req.getSubscriptionId())
                                .orElseThrow(() -> new Exception("Subscription not found"));
            payment.setSubscription(sub);
        }
        payment = paymentRepository.save(payment);

        PaymentInitiateResponse response = new PaymentInitiateResponse();
        if (req.getGateway() == PaymentGateway.RAZORPAY) {
            PaymentLinkResponse paymentLinkResponse = razorpayService.createPaymentLink(user, payment);
            response = PaymentInitiateResponse.builder()
                        .paymentId(payment.getId())
                        .gateway(payment.getGateway())
                        .checkoutUrl(paymentLinkResponse.getPayment_link_url())
                        .transactionId(paymentLinkResponse.getPayment_link_id())
                        .amount(payment.getAmount())
                        .description(payment.getDescription())
                        .success(true)
                        .message("Payment Initiated successfully")
                        .build();
            payment.setGatewayOrderId(paymentLinkResponse.getPayment_link_id());
        }

        payment.setStatus(PaymentStatus.PROCESSING);
        paymentRepository.save(payment);
        return response;
    }

    @Override
    public PaymentDto verifyPayment(PaymentVerifyRequest req) throws Exception {

        JSONObject paymentDetails = razorpayService.fetchPaymentDetails(req.getRazorpayPaymentId());

        JSONObject notes = paymentDetails.getJSONObject("notes");
        Long paymentId = notes.getLong("payment_id");

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new Exception("Payment record matching transaction notes not found"));

        boolean isValid = razorpayService.isValidPayment(req.getRazorpayPaymentId());

        if (isValid) {
            if (PaymentGateway.RAZORPAY == payment.getGateway()) {
                payment.setTransactionId(req.getRazorpayPaymentId());
            }
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setCompletedAt(LocalDateTime.now());
            
            paymentRepository.save(payment);

        paymentEventPublisher.publishPaymentSuccessEvent(payment);

        } else {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
        }
        
        return paymentMapper.toDto(payment);
    }

    @Override
    public Page<PaymentDto> getAllPayments(Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(paymentMapper::toDto);
    }

}
