package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.entity.Planner;
import com.example.mutsideout_mju.repository.PlannerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PlannerService {
    private final PlannerRepository plannerRepository;

    public void getAllPlanners() {
        List<Planner> planners = plannerRepository.findAll();
    }

    public void createPlanner(String content) {
        Planner planner = Planner.builder()
                .content(content)
                .isCompleted(false)
                .build();
        this.plannerRepository.save(planner);
    }

    public void updatePlanner(String content, UUID plannerId) {
        Planner planner = this.plannerRepository.findPlannerById(plannerId);
        this.plannerRepository.deleteById(plannerId);
    }

    public void getAllCompletedPlanner(){}

    public void completePlannnerById(UUID plannerId){}
}
