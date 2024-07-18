package com.example.mutsideout_mju.repository;

import com.example.mutsideout_mju.entity.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DiaryRepository extends JpaRepository<Diary, UUID> {
    Page<Diary> findByUserId(UUID userId, Pageable pageable);
}
