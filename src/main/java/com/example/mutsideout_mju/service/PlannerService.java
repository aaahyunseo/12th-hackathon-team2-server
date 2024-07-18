package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.planner.PlannerDto;
import com.example.mutsideout_mju.dto.response.planner.CompletedPlannerResponse;
import com.example.mutsideout_mju.dto.response.planner.PlannerResponseData;
import com.example.mutsideout_mju.entity.Planner;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.repository.PlannerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlannerService {
    // 지금은 User 추가 하기
    private final PlannerRepository plannerRepository;

    public List<PlannerResponseData> getAllPlanners(User user) {
        List<PlannerResponseData> plannerList = new ArrayList<>();
        List<Planner> allPlanners = Optional.ofNullable(plannerRepository.findAllByUserId(user.getId())).orElse(Collections.emptyList());

        for (Planner planner : allPlanners) {
            if (!planner.isCompleted()) {
                PlannerResponseData plannerResponseData = new PlannerResponseData(planner);
                plannerList.add(plannerResponseData);
            }
        }
        return plannerList;
    }

    public void createPlanner(PlannerDto plannerDto, User user) {
        Planner planner = Planner.builder()
                .content(plannerDto.getContent())
                .isCompleted(false)
                .user(user)
                .build();
        this.plannerRepository.save(planner);
    }

    public Planner updatePlanner(PlannerDto plannerDto, UUID plannerId, User user) {
        Planner planner = findPlanner(plannerId);

        if (!planner.getUser().getEmail().equals(user.getEmail())) {
            throw new RuntimeException("접근 할 수 없습니다.");
        }

        planner.setContent(plannerDto.getContent());
        return plannerRepository.save(planner);
    }

    public Planner completePlannerById(UUID plannerId, User user) {
        Planner planner = findPlanner(plannerId);

        if (!planner.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("접근 할 수 없습니다.");
        }

        planner.setCompleted(true);
        return plannerRepository.save(planner);
    }

    public Map<LocalDate, List<CompletedPlannerResponse>> getCompletedPlannersGroupedByDate(User user) {
        List<CompletedPlannerResponse> completedPlannerList = getAllCompletedPlanners(user);

        return completedPlannerList.stream()
                .collect(Collectors.groupingBy(planner -> planner.getModifiedDate().toLocalDate()));
    }

    private List<CompletedPlannerResponse> getAllCompletedPlanners(User user) {
        List<CompletedPlannerResponse> completedPlannerList = new ArrayList<>();
        List<Planner> completedPlanners = plannerRepository.findByIsCompletedAndUser(true, user);

        for (Planner planner : completedPlanners) {
            CompletedPlannerResponse plannerResponseData = new CompletedPlannerResponse(planner);
            completedPlannerList.add(plannerResponseData);
        }

        return completedPlannerList;
    }

    private Planner findPlanner(UUID plannerId) {
        return plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("플래너를 찾을 수 없습니다."));
    }
}
