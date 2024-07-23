package com.example.mutsideout_mju.repository;

import com.example.mutsideout_mju.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {
}
