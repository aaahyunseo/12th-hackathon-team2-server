package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.request.planner.CompletePlannerRequestDto;
import com.example.mutsideout_mju.dto.request.planner.PlannerDto;
import com.example.mutsideout_mju.dto.response.planner.CompletedPlannerResponse;
import com.example.mutsideout_mju.dto.response.planner.GroupedCompletedPlannerResponse;
import com.example.mutsideout_mju.dto.response.planner.PlannerListResponseData;
import com.example.mutsideout_mju.dto.response.planner.PlannerResponseData;
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

    public Planner updatePlanner(PlannerDto plannerDto, UUID plannerId, User user) {
        Planner planner = findPlanner(plannerId);

        if (!planner.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException(ErrorCode.NO_ACCESS, "해당 플래너에 접근 할 수 없습니다.");
        }

        planner.setContent(plannerDto.getContent());
        return plannerRepository.save(planner);
    }

    public void completePlannerById(UUID plannerId, CompletePlannerRequestDto requestDto, User user) {
        if (!requestDto.getIsCompleted()) {
            throw new UnauthorizedException(ErrorCode.INVALID_PLANNER_ACCESS);
        }
        Optional<Planner> optionalPlanner = plannerRepository.findById(plannerId);
        if (optionalPlanner.isPresent()) {
            Planner planner = optionalPlanner.get();

            if (!planner.getUser().getId().equals(user.getId())) {
                throw new UnauthorizedException(ErrorCode.NO_ACCESS, "해당 플래너에 접근 할 수 없습니다.");
            }
            planner.setCompleted(requestDto.getIsCompleted());
            plannerRepository.save(planner);
        } else {
            throw new RuntimeException("Planner not found");
        }
    }

    public GroupedCompletedPlannerResponse getCompletedPlannersGroupedByDate(User user) {
        List<CompletedPlannerResponse> completedPlannerList = getAllCompletedPlanners(user);
        Map<LocalDate, List<CompletedPlannerResponse>> groupedPlanners = completedPlannerList.stream()
                .collect(Collectors.groupingBy(planner -> planner.getModifiedDate().toLocalDate()));

        return new GroupedCompletedPlannerResponse(groupedPlanners);
    }

    private List<CompletedPlannerResponse> getAllCompletedPlanners(User user) {
        List<Planner> completedPlanners = plannerRepository.findByIsCompletedAndUser(true, user);
        return completedPlanners.stream()
                .map(CompletedPlannerResponse::fromPlanner)
                .collect(Collectors.toList());
    }

    private Planner findPlanner(UUID plannerId) {
        return plannerRepository.findById(plannerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PLANNER_NOT_FOUND));
    }
}
