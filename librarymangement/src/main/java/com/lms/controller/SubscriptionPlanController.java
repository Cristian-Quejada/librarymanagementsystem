package com.lms.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Service.SubscriptionPlanService;
import com.lms.payload.dto.SubscriptionPlanDto;
import com.lms.payload.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;
    
    @GetMapping
    public ResponseEntity<?> getAllSubscriptionPlans(){

        List<SubscriptionPlanDto> plans = subscriptionPlanService.getAllSubscriptionPlan();
        return ResponseEntity.ok(plans);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createSubscriptionPlan(
        @Valid @RequestBody SubscriptionPlanDto subscriptionPlanDto) throws Exception{

        SubscriptionPlanDto plans = subscriptionPlanService.createSubscriptionPlan(subscriptionPlanDto);
        return ResponseEntity.ok(plans);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateSubscriptionPlan(
        @Valid @RequestBody SubscriptionPlanDto subscriptionPlanDto, @PathVariable long id) throws Exception{

        SubscriptionPlanDto plans = subscriptionPlanService.updateSubscriptionPlan(id, subscriptionPlanDto);
        return ResponseEntity.ok(plans);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteSubscriptionPlan(@PathVariable long id) throws Exception{

        subscriptionPlanService.deleteSubscriptionPlan(id);
        ApiResponse response = new ApiResponse("Plan subscription deleted successfully", true);
        return ResponseEntity.ok(response);
    }
}
