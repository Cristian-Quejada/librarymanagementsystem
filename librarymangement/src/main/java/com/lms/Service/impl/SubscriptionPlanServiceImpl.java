package com.lms.Service.impl;

import com.lms.repository.SubscriptionRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lms.Model.SubscriptionPlan;
import com.lms.Model.User;
import com.lms.Service.SubscriptionPlanService;
import com.lms.Service.UserService;
import com.lms.mapper.SubscriptionPlanMapper;
import com.lms.payload.dto.SubscriptionPlanDto;
import com.lms.repository.SubscriptionPlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService{

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    private final SubscriptionPlanMapper subscriptionPlanMapper;

    private final UserService userService;



    @Override
    public SubscriptionPlanDto createSubscriptionPlan(SubscriptionPlanDto planDto) throws Exception {
        if (subscriptionPlanRepository.existsByPlanCode(planDto.getPlanCode())) {
            throw new Exception("Plan code is already exist!");
        }

        SubscriptionPlan plan = subscriptionPlanMapper.toEntity(planDto);

        User currentUser = userService.getCurrentUser();
        plan.setCreatedBy(currentUser.getFullName());
        plan.setUpdatedBy(currentUser.getFullName());
        SubscriptionPlan savedPlan = subscriptionPlanRepository.save(plan);
        return subscriptionPlanMapper.toDto(savedPlan);
    }

    @Override
    public SubscriptionPlanDto updateSubscriptionPlan(Long planId, SubscriptionPlanDto planDto) throws Exception {
        SubscriptionPlan existingPlan = subscriptionPlanRepository.findById(planId).orElseThrow(
                    () ->  new Exception("Plan code not found")
        );
        subscriptionPlanMapper.updateEntity(existingPlan, planDto);
        User currentUser = userService.getCurrentUser();
        existingPlan.setUpdatedBy(currentUser.getFullName());
        SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(existingPlan);
        return subscriptionPlanMapper.toDto(updatedPlan);
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) throws Exception {
        SubscriptionPlan existingPlan = subscriptionPlanRepository.findById(planId).orElseThrow(
                () -> new Exception("Plan code not found!")        
        );

        subscriptionPlanRepository.delete(existingPlan);
    }

    @Override
    public List<SubscriptionPlanDto> getAllSubscriptionPlan() {
        List<SubscriptionPlan> planList = subscriptionPlanRepository.findAll();

        return planList.stream().map(subscriptionPlanMapper::toDto)
                        .collect(Collectors.toList());
    }

    @Override
    public SubscriptionPlan getBySubscriptionPlanCode(String subscriptionPlanCode) throws Exception {
        SubscriptionPlan plan = subscriptionRepository.findByPlanCode(subscriptionPlanCode);
        if (plan == null) {
            throw new Exception("Plan not found");
        }
        return plan;
    }

}
