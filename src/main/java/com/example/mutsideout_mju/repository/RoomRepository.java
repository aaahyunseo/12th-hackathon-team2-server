package com.example.mutsideout_mju.repository;

import com.example.mutsideout_mju.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
}
