package com.example.mutsideout_mju.repository;

import com.example.mutsideout_mju.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageFileRepository extends JpaRepository<ImageFile, UUID> {
}
