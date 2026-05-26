package com.lms.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Service.SubscriptionService;
import com.lms.exception.SubscriptionException;
import com.lms.payload.dto.SubscriptionDto;
import com.lms.payload.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(
            @RequestBody SubscriptionDto subscription
    ) throws Exception {
        SubscriptionDto dto = subscriptionService.subscribe(subscription);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/active")
    public ResponseEntity<?> getUsersActiveSubscriptions(
            @RequestParam(required = false) Long userId
    ) throws Exception {
        SubscriptionDto dto = subscriptionService.getUsersActiveSubscription(userId);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAllSubscriptions() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<SubscriptionDto> dtoList = subscriptionService.getAllSubscriptions(pageable);

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/admin/deactivate-expired")
    public ResponseEntity<?> deactivateExpiredSubscriptions() throws Exception{

        subscriptionService.deactivateExpiredSubscription();
        ApiResponse response = new ApiResponse("Task done!", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{subscriptionId}")
    public ResponseEntity<?> cancelSubscription(
            @PathVariable Long subscriptionId,
            @RequestParam(required = false) String reason
    ) throws SubscriptionException {
        SubscriptionDto subscription = subscriptionService.cancelSubscription(subscriptionId, reason);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateSubscription(
            @RequestParam Long subscriptionId,
            @RequestParam Long paymentId
    ) throws SubscriptionException {
        SubscriptionDto subscription = subscriptionService.activateSubscription(subscriptionId, paymentId);
        return ResponseEntity.ok(subscription);
    }
}
