package com.lms.Service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.Service.SubscriptionPlanService;
import com.lms.payload.dto.SubscriptionPlanDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService{

    @Override
    public SubscriptionPlanDto createSubscriptionPlan(SubscriptionPlanDto planDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSubscriptionPlan'");
    }

    @Override
    public SubscriptionPlanDto updateSubscriptionPlan(Long planId, SubscriptionPlanDto planDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSubscriptionPlan'");
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteSubscriptionPlan'");
    }

    @Override
    public List<SubscriptionPlanDto> getAllSubscriptionPlan() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllSubscriptionPlan'");
    }

}
