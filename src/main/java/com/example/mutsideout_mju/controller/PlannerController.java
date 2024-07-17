package com.example.mutsideout_mju.controller;

import com.example.mutsideout_mju.dto.planner.PlannerDto;
import com.example.mutsideout_mju.dto.response.ResponseDto;
import com.example.mutsideout_mju.dto.response.planner.CompletedPlannerResponse;
import com.example.mutsideout_mju.dto.response.planner.PlannerResponseData;
import com.example.mutsideout_mju.service.PlannerService;
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
    public ResponseEntity<ResponseDto<List<PlannerResponseData>>> getAllPlanners() {
        List<PlannerResponseData> planners = plannerService.getAllPlanners();
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "uncompleted plans", planners), HttpStatus.OK);
    }

    // 새 planner 생성
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createPlanner(@RequestBody PlannerDto plannerDto) {
        plannerService.createPlanner(plannerDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "plan created"), HttpStatus.CREATED);
    }

    // planner 수정
    @PatchMapping("/{plannerId}")
    public ResponseEntity<ResponseDto<Void>> updatePlanner(@RequestBody PlannerDto plannerDto, @PathVariable UUID plannerId) {
        plannerService.updatePlanner(plannerDto, plannerId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "plan updated"), HttpStatus.OK);
    }

    // 완료 상태의 planner들 조회하기
    @GetMapping("/completed")
    public ResponseEntity<ResponseDto<Map<LocalDate, List<CompletedPlannerResponse>>>> getAllCompletedPlanners() {
        Map<LocalDate, List<CompletedPlannerResponse>> completedPlanners = plannerService.getCompletedPlannersGroupedByDate();
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "completed plans", completedPlanners), HttpStatus.OK);
    }

    // planner 완료하기
    @PutMapping("/{plannerId}/complete")
    public ResponseEntity<ResponseDto<Void>> completePlannerById(@PathVariable UUID plannerId) {
        plannerService.completePlannerById(plannerId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "complete plan"), HttpStatus.OK);
    }
}
