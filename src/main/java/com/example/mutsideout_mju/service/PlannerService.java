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
            if(!planner.isCompleted())
            {
                PlannerResponseData plannerResponseData = PlannerResponseData.builder()
                        .plannerId(planner.getId())
                        .content(planner.getContent())
                        .isCompleted(planner.isCompleted())
                        .createdAt(planner.getCreatedAt())
                        .build();
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
        Optional<Planner> optionalPlanner = plannerRepository.findById(plannerId);
        if (!optionalPlanner.isPresent()) {
            throw new RuntimeException("잘못된 plannerId 입니다.");
        }

        Planner planner = optionalPlanner.get();

        if (!planner.getUser().equals(user)) {
            throw new RuntimeException("접근 할 수 없습니다.");
        }

        planner.setContent(plannerDto.getContent());
        return plannerRepository.save(planner);
    }

    public Map<LocalDate, List<CompletedPlannerResponse>> getCompletedPlannersGroupedByDate(User user) {
        List<CompletedPlannerResponse> completedPlannerList = getAllCompletedPlanners();
        List<CompletedPlannerResponse> filteredList = new ArrayList<>();

        for (CompletedPlannerResponse completedPlannerResponse : completedPlannerList) {
            if (completedPlannerResponse.getUser().equals(user)) {
                filteredList.add(completedPlannerResponse);
            }
        }

        return filteredList.stream()
                .collect(Collectors.groupingBy(planner -> planner.getModifiedDate().toLocalDate()));
    }

    public Planner completePlannerById(UUID plannerId, User user) {
        Optional<Planner> optionalPlanner = plannerRepository.findById(plannerId);
        if (optionalPlanner.isPresent() && optionalPlanner.get().getUser().equals(user)) {
            Planner planner = optionalPlanner.get();
            planner.setCompleted(true);
            return plannerRepository.save(planner);
        } else {
            throw new RuntimeException("접근 할 수 없습니다.");
        }
    }

    // PlannerService 내에서 사용하는 메소드.
    private List<CompletedPlannerResponse> getAllCompletedPlanners() {
        List<CompletedPlannerResponse> completedPlannerList = new ArrayList<>();
        List<Planner> completedPlanners = plannerRepository.findByIsCompleted(true);
        for (Planner planner : completedPlanners) {
            CompletedPlannerResponse plannerResponseData = CompletedPlannerResponse.builder()
                    .plannerId(planner.getId())
                    .content(planner.getContent())
                    .isCompleted(planner.isCompleted())
                    .modifiedDate(planner.getModifiedDate())
                    .user(planner.getUser())
                    .build();
            completedPlannerList.add(plannerResponseData);
        }
        return completedPlannerList;
    }
}
