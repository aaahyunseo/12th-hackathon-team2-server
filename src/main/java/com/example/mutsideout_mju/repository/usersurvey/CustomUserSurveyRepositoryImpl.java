package com.example.mutsideout_mju.repository.usersurvey;


import com.example.mutsideout_mju.entity.SurveyOption;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.UUID;

import static com.example.mutsideout_mju.entity.QUserSurvey.userSurvey;

public class CustomUserSurveyRepositoryImpl implements CustomUserSurveyRepository {

    private final JPAQueryFactory queryFactory;

    public CustomUserSurveyRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public long countValidSurveyResponse(UUID userId) {
        return queryFactory
                .selectFrom(userSurvey)
                .where(userSurvey.user.id.eq(userId),
                        userSurvey.survey.number.between(1, 3)
                                .and(userSurvey.surveyOption.in(SurveyOption.NORMAL, SurveyOption.YES))
                                .or(userSurvey.survey.number.between(4, 6)
                                        .and(userSurvey.surveyOption.eq(SurveyOption.YES))))
                .fetchCount();
    }
}
