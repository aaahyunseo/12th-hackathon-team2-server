package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.request.planner.CompletePlannerRequestDto;
import com.example.mutsideout_mju.dto.request.planner.PlannerDto;
import com.example.mutsideout_mju.dto.response.planner.*;
import com.example.mutsideout_mju.entity.Planner;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.UnauthorizedException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.PlannerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlannerService {
    private final PlannerRepository plannerRepository;

    public PlannerListResponseData getAllPlanners(User user) {
        List<Planner> allPlanners = plannerRepository.findAllByUserId(user.getId());

        List<PlannerResponseData> plannerResponseDataList = allPlanners.stream()
                .filter(planner -> !planner.isCompleted())
                .sorted(Comparator.comparing(Planner::getCreatedAt).reversed())
                .map(PlannerResponseData::fromPlanner)
                .collect(Collectors.toList());

        return new PlannerListResponseData(plannerResponseDataList);
    }

    public void createPlanner(PlannerDto plannerDto, User user) {
        Planner planner = Planner.builder()
                .content(plannerDto.getContent())
                .isCompleted(false)
                .user(user)
                .build();
        this.plannerRepository.save(planner);
    }

    public void updatePlanner(PlannerDto plannerDto, UUID plannerId, User user) {
        Planner planner = findPlanner(plannerId);
        if (planner.isCompleted()) {
            throw new UnauthorizedException(ErrorCode.INVALID_PLANNER_ACCESS);
        }

        validateUserAccess(planner, user);
        planner.setContent(plannerDto.getContent());
        plannerRepository.save(planner);
    }

    public void completePlannerById(UUID plannerId, CompletePlannerRequestDto requestDto, User user) {
        Planner planner = findPlanner(plannerId);
        if (!requestDto.getIsCompleted() || planner.isCompleted()) {
            throw new UnauthorizedException(ErrorCode.INVALID_PLANNER_ACCESS);
        }
        validateUserAccess(planner, user);
        planner.setCompleted(requestDto.getIsCompleted());
        plannerRepository.save(planner);
    }

    public GroupedCompletedPlannerResponse getCompletedPlannersGroupedByDate(User user) {
        CompletedPlannerListResponseData completedPlannerList = getAllCompletedPlanners(user);

        // LocalDateTime을 기준으로 정렬하고, 날짜별로 그룹화
        Map<String, List<CompletedPlannerResponse>> groupedPlanners = completedPlannerList.getCompletedPlanners()
                .stream()
                .sorted(Comparator.comparing(CompletedPlannerResponse::getModifiedDate).reversed()) // LocalDateTime으로 정렬
                .collect(Collectors.groupingBy(planner -> planner.getModifiedDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        // 그룹별로 정렬하고, 각 그룹 내 항목을 LocalDateTime으로 정렬
        Map<String, List<CompletedPlannerResponseForClient>> sortedGroupedPlanners = new LinkedHashMap<>();
        groupedPlanners.entrySet().stream()
                .sorted(Map.Entry.<String, List<CompletedPlannerResponse>>comparingByKey().reversed())
                .forEachOrdered(entry -> {
                    List<CompletedPlannerResponseForClient> sortedPlanners = entry.getValue().stream()
                            .map(CompletedPlannerResponse::toClientResponse) // 변환하여 클라이언트 응답 생성
                            .sorted(Comparator.comparing(CompletedPlannerResponseForClient::getFormattedDate).reversed()) // LocalDateTime으로 정렬
                            .collect(Collectors.toList());
                    sortedGroupedPlanners.put(entry.getKey(), sortedPlanners);
                });

        return new GroupedCompletedPlannerResponse(sortedGroupedPlanners);
    }


    public CompletedPlannerListResponseData getAllCompletedPlanners(User user) {
        List<Planner> completedPlanners = plannerRepository.findByIsCompletedAndUser(true, user);
        List<CompletedPlannerResponse> completedPlannerResponses = completedPlanners.stream()
                .map(CompletedPlannerResponse::fromPlanner)
                .collect(Collectors.toList());
        return new CompletedPlannerListResponseData(completedPlannerResponses);
    }

    private Planner findPlanner(UUID plannerId) {
        return plannerRepository.findById(plannerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PLANNER_NOT_FOUND));
    }

    private void validateUserAccess(Planner planner, User user) {
        if (!planner.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException(ErrorCode.NO_ACCESS, "해당 플래너에 접근 할 수 없습니다.");
        }
    }
}
