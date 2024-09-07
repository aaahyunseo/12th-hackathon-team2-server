package com.example.mutsideout_mju.controller;

import com.example.mutsideout_mju.authentication.AuthenticatedUser;
import com.example.mutsideout_mju.dto.request.planner.CompletePlannerRequestDto;
import com.example.mutsideout_mju.dto.request.planner.PlannerDto;
import com.example.mutsideout_mju.dto.response.ResponseDto;
import com.example.mutsideout_mju.dto.response.planner.DailyPlannerCompletionDataList;
import com.example.mutsideout_mju.dto.response.planner.GroupedCompletedPlannerResponse;
import com.example.mutsideout_mju.dto.response.planner.PlannerListResponseData;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.service.PlannerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/planners")
public class PlannerController {
    private final PlannerService plannerService;

    // 미완료 상태의 플래너 전체 조회
    @GetMapping
    public ResponseEntity<ResponseDto<PlannerListResponseData>> getAllPlanners(@AuthenticatedUser User user) {
        PlannerListResponseData planners = plannerService.getAllPlanners(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "미완료 상태 플래너 전체 조회", planners), HttpStatus.OK);
    }

    // 플래너 생성
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createPlanner(@AuthenticatedUser User user, @RequestBody @Valid PlannerDto plannerDto) {
        plannerService.createPlanner(user, plannerDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "플래너 생성"), HttpStatus.CREATED);
    }

    // 플래너 수정
    @PatchMapping("/{plannerId}")
    public ResponseEntity<ResponseDto<Void>> updatePlanner(@AuthenticatedUser User user, @RequestBody @Valid PlannerDto plannerDto, @PathVariable UUID plannerId) {
        plannerService.updatePlanner(user, plannerId, plannerDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "플랜 수정 완료"), HttpStatus.OK);
    }

    // 완료 상태의 플래너 전체 목록 조회
    @GetMapping("/completed")
    public ResponseEntity<ResponseDto<GroupedCompletedPlannerResponse>> getAllCompletedPlanners(@AuthenticatedUser User user) {
        GroupedCompletedPlannerResponse completedPlanners = plannerService.getCompletedPlannersGroupedByDate(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "완료된 플래너 전체 조회", completedPlanners), HttpStatus.OK);
    }

    // 플래너 삭제
    @DeleteMapping("/{plannerId}")
    public ResponseEntity<ResponseDto<Void>> deletePlanner(@AuthenticatedUser User user, @PathVariable UUID plannerId) {
        plannerService.deletePlanner(user, plannerId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "플랜 삭제 완료"), HttpStatus.OK);
    }

    // 플래너 완료
    @PatchMapping("{plannerId}/complete")
    public ResponseEntity<ResponseDto<Void>> completePlannerById(
            @AuthenticatedUser User user,
            @PathVariable UUID plannerId) {
        plannerService.completePlannerById(user, plannerId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "플래너 완료 상태로 변경"), HttpStatus.OK);
    }

    // 월별 플래너 잔디 타입 조회
    @GetMapping("/calendars")
    public ResponseEntity<ResponseDto<DailyPlannerCompletionDataList>> getMonthlyPlannersStats(
            @AuthenticatedUser User user,
            @RequestParam String month) {
        DailyPlannerCompletionDataList dailyPlannerCompletionDataList = plannerService.getMonthlyPlannerStats(user, month);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "월별 플래너 잔디 타입 조회 완료", dailyPlannerCompletionDataList), HttpStatus.OK);
    }
}
