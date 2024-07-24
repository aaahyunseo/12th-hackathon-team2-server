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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerService {
    private final PlannerRepository plannerRepository;

    public PlannerListResponseData getAllPlanners(User user) {
        List<Planner> allPlanners = plannerRepository.findAllByUserId(user.getId());

        List<PlannerResponseData> plannerResponseDataList = allPlanners.stream()
                .filter(planner -> !planner.isCompleted())
                .map(PlannerResponseData::fromPlanner)
                .collect(Collectors.toList());

        return new PlannerListResponseData(plannerResponseDataList);
    }

    public void createPlanner(User user, PlannerDto plannerDto) {
        Planner planner = Planner.builder()
                .content(plannerDto.getContent())
                .isCompleted(false)
                .user(user)
                .build();
        this.plannerRepository.save(planner);
    }

    public void updatePlanner(User user, UUID plannerId, PlannerDto plannerDto) {
        Planner planner = findPlanner(user.getId(), plannerId);
        if (planner.isCompleted()) {
            throw new UnauthorizedException(ErrorCode.INVALID_PLANNER_ACCESS);
        }

        planner.setContent(plannerDto.getContent());
        plannerRepository.save(planner);
    }

    public void completePlannerById(User user, UUID plannerId, CompletePlannerRequestDto requestDto) {
        Planner planner = findPlanner(user.getId(), plannerId);
        if (!requestDto.getIsCompleted() || planner.isCompleted()) {
            throw new UnauthorizedException(ErrorCode.INVALID_PLANNER_ACCESS);
        }

        planner.setCompleted(requestDto.getIsCompleted());
        plannerRepository.save(planner);
    }

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

    private CompletedPlannerListResponseData getAllCompletedPlanners(User user) {
        List<Planner> completedPlanners = plannerRepository.findByIsCompletedAndUser(true, user);
        List<CompletedPlannerResponse> completedPlannerResponses = completedPlanners.stream()
                .map(CompletedPlannerResponse::fromPlanner)
                .collect(Collectors.toList());

        return new CompletedPlannerListResponseData(completedPlannerResponses);
    }

    private Planner findPlanner(UUID userId, UUID plannerId) {
        return plannerRepository.findByUserIdAndId(userId, plannerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PLANNER_NOT_FOUND));
    }
}
