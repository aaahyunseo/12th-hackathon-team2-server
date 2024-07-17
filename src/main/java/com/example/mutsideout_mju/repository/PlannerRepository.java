package com.example.mutsideout_mju.repository;

import com.example.mutsideout_mju.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlannerRepository extends JpaRepository<Planner, UUID> {
    List<Planner> findByIsCompleted(boolean isCompleted);
}
