package com.example.mutsideout_mju.repository;

import com.example.mutsideout_mju.entity.Planner;
import com.example.mutsideout_mju.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlannerRepository extends JpaRepository<Planner, UUID>, CustomPlannerRepository {
    List<Planner> findAllByUserId(UUID userId);

    List<Planner> findByIsCompletedAndUser(boolean b, User user);

    Optional<Planner> findByUserIdAndId(UUID userId, UUID plannerId);
}
