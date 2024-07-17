package com.example.mutsideout_mju.controller;

import com.example.mutsideout_mju.dto.request.auth.SignupDto;
import com.example.mutsideout_mju.dto.response.ResponseDto;
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

    // 모든 미완료 상태의 planner 조회하기
    @GetMapping
    public ResponseEntity<ResponseDto<Void>> getAllPlanners() {
        this.plannerService.getAllPlanners();
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "ok"), HttpStatus.OK);
    }

    // 새 planner 생성
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createPlanner(@RequestBody String content) {
        this.plannerService.createPlanner(content);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "planner created"), HttpStatus.CREATED);
    }

    // planner 수정
    @PatchMapping("/{plannerId}")
    public ResponseEntity<ResponseDto<Void>> updatePlanner(@RequestBody String content, @PathVariable UUID plannerId) {
        this.plannerService.updatePlanner(content, plannerId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "planner updated"), HttpStatus.OK);
    }

    //  완료 상태의 planner들 조회하기
    @PostMapping("/completed")
    public ResponseEntity<ResponseDto<Void>> getAllCompletedPlanners(){
        this.plannerService.getAllCompletedPlanners();
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "ok"), HttpStatus.OK);
    }

    // planner 완료하기
    @PutMapping("/{plannerId}/complete")
    public ResponseEntity<ResponseDto<Void>> completePlannerById(@PathVariable UUID plannerId){
        this.plannerService.completePlannerById(plannerId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "ok"), HttpStatus.OK);
    }
}
