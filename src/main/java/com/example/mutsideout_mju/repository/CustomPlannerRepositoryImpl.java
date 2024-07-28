package com.example.mutsideout_mju.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.mutsideout_mju.entity.QPlanner.planner;

@Repository
public class CustomPlannerRepositoryImpl implements CustomPlannerRepository {

    private final JPAQueryFactory queryFactory;

    public CustomPlannerRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Map<String, Long> getCompletedPlannersStats(UUID userId, LocalDate startDate, LocalDate endDate) {
        List<Tuple> results = queryFactory
                .select(planner.modifiedDate.year(),
                        planner.modifiedDate.month(),
                        planner.modifiedDate.dayOfMonth(),
                        planner.count())
                .from(planner)
                .where( // 로그인한 유저의 플래너들을 선택
                        planner.user.id.eq(userId)
                        // 이미 완료된 플래너들을 선택
                        .and(planner.isCompleted.isTrue())
                        // modifiedDate 가 startDate 자정 ~ endDate 23시 59분 59초 사이인 플래너들을 선택
                        .and(planner.modifiedDate.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59))))
                // modifiedDate 의 년,월,일을 기준으로 그룹화 후 카운트
                .groupBy(planner.modifiedDate.year(),
                        planner.modifiedDate.month(),
                        planner.modifiedDate.dayOfMonth())
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> String.format("%d-%02d-%02d", tuple.get(0, Integer.class), tuple.get(1, Integer.class), tuple.get(2, Integer.class)),
                        tuple -> tuple.get(3, Long.class)
                ));
    }
}
