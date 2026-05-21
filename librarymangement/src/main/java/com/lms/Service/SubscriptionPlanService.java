package com.lms.Service;

import java.util.List;

import com.lms.payload.dto.SubscriptionPlanDto;

public interface SubscriptionPlanService {

    SubscriptionPlanDto createSubscriptionPlan(SubscriptionPlanDto planDto) throws Exception;

    SubscriptionPlanDto updateSubscriptionPlan(Long planId, SubscriptionPlanDto planDto) throws Exception;

    void deleteSubscriptionPlan(Long planId) throws Exception;

    List<SubscriptionPlanDto> getAllSubscriptionPlan();
}
