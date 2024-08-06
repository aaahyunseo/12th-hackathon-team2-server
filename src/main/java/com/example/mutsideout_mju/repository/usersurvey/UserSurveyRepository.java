package com.example.mutsideout_mju.repository.usersurvey;

import com.example.mutsideout_mju.entity.UserSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserSurveyRepository extends JpaRepository<UserSurvey, UUID>, CustomUserSurveyRepository {
    boolean existsByUserId(UUID userId);
    List<UserSurvey> findByUserIdAndSurveyIdIn(UUID userId, List<UUID> surveyIds);
}
