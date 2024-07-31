package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.request.planner.CompletePlannerRequestDto;
import com.example.mutsideout_mju.dto.request.planner.PlannerDto;
import com.example.mutsideout_mju.dto.response.planner.*;
import com.example.mutsideout_mju.entity.Planner;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.exception.ForbiddenException;
import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.UnauthorizedException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.planner.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerService {
    private final PlannerRepository plannerRepository;

    /**
     * 미완료 상태의 플래너 전체 조회
     */
    public PlannerListResponseData getAllPlanners(User user) {
        List<Planner> allPlanners = plannerRepository.findAllByUserId(user.getId());

        List<PlannerResponseData> plannerResponseDataList = allPlanners.stream()
                .filter(planner -> !planner.isCompleted())
                .sorted(Comparator.comparing(Planner::getCreatedAt).reversed())
                .map(PlannerResponseData::fromPlanner)
                .collect(Collectors.toList());

        return new PlannerListResponseData(plannerResponseDataList);
    }

    /**
     * 플래너 생성
     */
    public void createPlanner(User user, PlannerDto plannerDto) {
        Planner planner = Planner.builder()
                .content(plannerDto.getContent())
                .isCompleted(false)
                .user(user)
                .build();
        this.plannerRepository.save(planner);
    }

    /**
     * 플래너 수정
     */
    public void updatePlanner(User user, UUID plannerId, PlannerDto plannerDto) {
        Planner planner = findPlanner(user.getId(), plannerId);
        if (planner.isCompleted()) {
            throw new ForbiddenException(ErrorCode.INVALID_PLANNER_ACCESS, "완료된 플랜은 수정할 수 없습니다.");
        }

        planner.setContent(plannerDto.getContent());
        plannerRepository.save(planner);
    }

    /**
     * 플래너 완료
     */
    public void completePlannerById(User user, UUID plannerId, CompletePlannerRequestDto requestDto) {
        Planner planner = findPlanner(user.getId(), plannerId);
        if (!requestDto.getIsCompleted() || planner.isCompleted()) {
            throw new UnauthorizedException(ErrorCode.INVALID_PLANNER_ACCESS);
        }

        planner.setCompleted(requestDto.getIsCompleted());
        plannerRepository.save(planner);
    }

    /**
     * 완료 상태의 플래너 전체 목록 조회
     */
    public GroupedCompletedPlannerResponse getCompletedPlannersGroupedByDate(User user) {
        CompletedPlannerListResponseData completedPlannerList = getAllCompletedPlanners(user);

        Map<String, List<CompletedPlannerResponse>> groupedPlanners = completedPlannerList.getCompletedPlanners()
                .stream()
                .sorted(Comparator.comparing(CompletedPlannerResponse::getModifiedDate).reversed())
                .collect(Collectors.groupingBy(planner -> planner.getModifiedDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        Map<String, List<CompletedPlannerResponseForClient>> sortedGroupedPlanners = new LinkedHashMap<>();
        groupedPlanners.entrySet().stream()
                .sorted(Map.Entry.<String, List<CompletedPlannerResponse>>comparingByKey().reversed())
                .forEachOrdered(entry -> {
                    List<CompletedPlannerResponseForClient> sortedPlanners = entry.getValue().stream()
                            .map(CompletedPlannerResponse::toClientResponse)
                            .sorted(Comparator.comparing(CompletedPlannerResponseForClient::getModifiedDate).reversed())
                            .collect(Collectors.toList());
                    sortedGroupedPlanners.put(entry.getKey(), sortedPlanners);
                });

        return new GroupedCompletedPlannerResponse(sortedGroupedPlanners);
    }

    /**
     * 완료된 플래너 전체 목록 조회
     */
    private CompletedPlannerListResponseData getAllCompletedPlanners(User user) {
        List<Planner> completedPlanners = plannerRepository.findByIsCompletedAndUser(true, user);
        List<CompletedPlannerResponse> completedPlannerResponses = completedPlanners.stream()
                .map(CompletedPlannerResponse::fromPlanner)
                .collect(Collectors.toList());

        return new CompletedPlannerListResponseData(completedPlannerResponses);
    }

    /**
     * 월별 완료된 플래너 잔디 타입 조회
     */
    public DailyPlannerCompletionDataList getMonthlyPlannerStats(User user, String month) {
        LocalDate startDate = LocalDate.parse(month + "-01");
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        // 현재 날짜와 해당 월의 마지막 날 중 더 이른 날짜를 endDate 로 설정
        LocalDate today = LocalDate.now();
        if (endDate.isAfter(today)) {
            endDate = today;
        }

        Map<String, Long> dailyCounts = plannerRepository.getCompletedPlannersStats(user.getId(), startDate, endDate);

        return DailyPlannerCompletionDataList.from(dailyCounts);
    }

    /**
     * 플래너 삭제
     */
    public void deletePlanner(User user, UUID plannerId) {
        Planner planner = findPlanner(user.getId(), plannerId);
        if(planner.isCompleted()){
            throw new ForbiddenException(ErrorCode.INVALID_PLANNER_ACCESS, "완료된 플랜은 삭제할 수 없습니다.");
        }
        plannerRepository.delete(planner);
    }

    private Planner findPlanner(UUID userId, UUID plannerId) {
        return plannerRepository.findByUserIdAndId(userId, plannerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PLANNER_NOT_FOUND));
    }
}
