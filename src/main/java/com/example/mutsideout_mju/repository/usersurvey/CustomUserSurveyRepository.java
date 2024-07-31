package com.example.mutsideout_mju.repository.usersurvey;

import java.util.UUID;

public interface CustomUserSurveyRepository {
    long countValidResponsesByUserId(UUID userId);
}
