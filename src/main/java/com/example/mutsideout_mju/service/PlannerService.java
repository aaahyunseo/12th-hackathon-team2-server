package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.entity.Planner;
import com.example.mutsideout_mju.repository.PlannerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PlannerService {
    private final PlannerRepository plannerRepository;

    public List<Planner> getAllPlanners() {
        return plannerRepository.findAll();
    }

    public void createPlanner(String content) {
        Planner planner = Planner.builder()
                .content(content)
                .isCompleted(false)
                .build();
        this.plannerRepository.save(planner);
    }

    public Planner updatePlanner(String content, UUID plannerId) {
        Optional<Planner> optionalPlanner = plannerRepository.findById(plannerId);
        if (optionalPlanner.isPresent()) {
            Planner planner = optionalPlanner.get();
            planner.setContent(content);
            return plannerRepository.save(planner);
        }
        return null;
    }

    public List<Planner> getAllCompletedPlanners() {
        return plannerRepository.findByIsCompleted(true);
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
