package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.planner.PlannerDto;
import com.example.mutsideout_mju.dto.response.planner.CompletedPlannerResponse;
import com.example.mutsideout_mju.dto.response.planner.PlannerResponseData;
import com.example.mutsideout_mju.entity.Planner;
import com.example.mutsideout_mju.repository.PlannerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlannerService {
    private final PlannerRepository plannerRepository;

    public List<PlannerResponseData> getAllPlanners() {
        List<PlannerResponseData> plannerList = new ArrayList<>();
        List<Planner> allPlanners = plannerRepository.findAll();
        for (Planner planner : allPlanners) {
            if(!planner.isCompleted())
            {
                PlannerResponseData plannerResponseData = PlannerResponseData.builder()
                        .plannerId(planner.getId())
                        .content(planner.getContent())
                        .isCompleted(planner.isCompleted())
                        .createdAt(planner.getModifiedDate())
                        .build();
                plannerList.add(plannerResponseData);
            }
        }
        return plannerList;
    }

    public void createPlanner(PlannerDto plannerDto) {
        Planner planner = Planner.builder()
                .content(plannerDto.getContent())
                .isCompleted(false)
                .build();
        this.plannerRepository.save(planner);
    }

    public Planner updatePlanner(PlannerDto plannerDto, UUID plannerId) {
        Optional<Planner> optionalPlanner = plannerRepository.findById(plannerId);
        if (optionalPlanner.isPresent()) {
            Planner planner = optionalPlanner.get();
            planner.setContent(plannerDto.getContent());
            return plannerRepository.save(planner);
        }
        return null;
    }

    public List<CompletedPlannerResponse> getAllCompletedPlanners() {
        List<CompletedPlannerResponse> completedPlannerList = new ArrayList<>();
        List<Planner> completedPlanners = plannerRepository.findByIsCompleted(true);
        for (Planner planner : completedPlanners) {
            CompletedPlannerResponse plannerResponseData = CompletedPlannerResponse.builder()
                    .plannerId(planner.getId())
                    .content(planner.getContent())
                    .isCompleted(planner.isCompleted())
                    .modifiedDate(planner.getModifiedDate())
                    .build();
            completedPlannerList.add(plannerResponseData);
        }
        return completedPlannerList;
    }

    public Map<LocalDate, List<CompletedPlannerResponse>> getCompletedPlannersGroupedByDate() {
        List<CompletedPlannerResponse> completedPlannerList = getAllCompletedPlanners();
        return completedPlannerList.stream()
                .collect(Collectors.groupingBy(planner -> planner.getModifiedDate().toLocalDate()));
    }

    public Planner completePlannerById(UUID plannerId) {
        Optional<Planner> optionalPlanner = plannerRepository.findById(plannerId);
        if (optionalPlanner.isPresent()) {
            Planner planner = optionalPlanner.get();
            planner.setCompleted(true);
            return plannerRepository.save(planner);
        }
        return null;
    }

}
