package com.lms.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lms.payload.dto.PaymentDto;
import com.lms.payload.request.PaymentInitiateRequest;
import com.lms.payload.request.PaymentVerifyRequest;
import com.lms.payload.response.PaymentInitiateResponse;

public interface PaymentService {

    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest req) throws Exception;

    PaymentDto verifyPayment(PaymentVerifyRequest req) throws Exception;

    Page<PaymentDto> getAllPayments(Pageable pageable);
}
