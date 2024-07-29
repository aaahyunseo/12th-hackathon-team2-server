package com.example.mutsideout_mju.repository;

import com.example.mutsideout_mju.entity.Planner;
import com.example.mutsideout_mju.entity.Room;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    Optional<Room> findByUserIdAndId(UUID userId, UUID roomId);

    @Transactional
    void deleteByCreatedAtBefore(LocalDateTime dateTime);
}
