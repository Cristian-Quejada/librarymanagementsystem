package com.lms.Service;

import java.util.List;

import com.lms.payload.dto.SubscriptionPlanDto;

public interface SubscriptionPlanService {

    SubscriptionPlanDto createSubscriptionPlan(SubscriptionPlanDto planDto);

    SubscriptionPlanDto updateSubscriptionPlan(Long planId, SubscriptionPlanDto planDto);

    void deleteSubscriptionPlan(Long planId);

    List<SubscriptionPlanDto> getAllSubscriptionPlan();
}
