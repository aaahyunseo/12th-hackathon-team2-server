package com.example.mutsideout_mju.repository;

import com.example.mutsideout_mju.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlannerRepository extends JpaRepository<Planner, UUID> {
    Planner findPlannerById(UUID id);
}
