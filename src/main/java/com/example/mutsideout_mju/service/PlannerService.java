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

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
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
        Map<LocalDate, List<CompletedPlannerResponse>> groupedPlanners = completedPlannerList.getCompletedPlanners()
                .stream()
                .collect(Collectors.groupingBy(planner -> planner.getModifiedDate().toLocalDate()));

        return new GroupedCompletedPlannerResponse(groupedPlanners);
    }

    private CompletedPlannerListResponseData getAllCompletedPlanners(User user) {
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
