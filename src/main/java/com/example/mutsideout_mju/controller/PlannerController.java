package com.example.mutsideout_mju.controller;

import com.example.mutsideout_mju.authentication.AuthenticatedUser;
import com.example.mutsideout_mju.dto.request.planner.CompletePlannerRequestDto;
import com.example.mutsideout_mju.dto.request.planner.PlannerDto;
import com.example.mutsideout_mju.dto.response.ResponseDto;
import com.example.mutsideout_mju.dto.response.planner.CompletedPlannerResponse;
import com.example.mutsideout_mju.dto.response.planner.GroupedCompletedPlannerResponse;
import com.example.mutsideout_mju.dto.response.planner.PlannerListResponseData;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.service.PlannerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/planners")
public class PlannerController {
    private final PlannerService plannerService;

    // 모든 미완료 상태의 planner 조회하기
    @GetMapping
    public ResponseEntity<ResponseDto<PlannerListResponseData>> getAllPlanners(@AuthenticatedUser User user) {
        PlannerListResponseData planners = plannerService.getAllPlanners(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "미완료 상태 플래너 전체 조회", planners), HttpStatus.OK);
    }

    // 새 planner 생성
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createPlanner(@AuthenticatedUser User user, @RequestBody @Valid PlannerDto plannerDto) {
        plannerService.createPlanner(plannerDto, user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "플래너 생성"), HttpStatus.CREATED);
    }

    // planner 수정
    @PatchMapping("/{plannerId}")
    public ResponseEntity<ResponseDto<Void>> updatePlanner(@AuthenticatedUser User user, @RequestBody @Valid PlannerDto plannerDto, @PathVariable UUID plannerId) {
        plannerService.updatePlanner(plannerDto, plannerId, user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "플래너 수정 완료"), HttpStatus.OK);
    }

    // 완료 상태의 planner들 조회하기
    @GetMapping("/completed")
    public ResponseEntity<ResponseDto<GroupedCompletedPlannerResponse>> getAllCompletedPlanners(@AuthenticatedUser User user) {
        GroupedCompletedPlannerResponse completedPlanners = plannerService.getCompletedPlannersGroupedByDate(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "완료된 플래너 전체 조회", completedPlanners), HttpStatus.OK);
    }

    // planner 완료하기
    @PutMapping("/{plannerId}")
    public ResponseEntity<ResponseDto<Void>> completePlannerById(
            @AuthenticatedUser User user,
            @RequestBody @Valid CompletePlannerRequestDto requestDto,
            @PathVariable UUID plannerId) {
        plannerService.completePlannerById(plannerId, requestDto, user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "플래너 완료 상태로 변경"), HttpStatus.OK);
    }
}
