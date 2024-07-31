package com.example.mutsideout_mju.repository;

import com.example.mutsideout_mju.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, UUID> {
}
