package com.example.mutsideout_mju.controller;

import com.example.mutsideout_mju.service.PlannerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/planners")
public class PlannerController {
    private final PlannerService plannerService;

    @GetMapping
    public void getAllPlanners() {
        this.plannerService.getAllPlanners();
    }

    @PostMapping
    public void createPlanner(@RequestBody String content) {
        this.plannerService.createPlanner(content);
    }

    @PatchMapping("/{plannerId}")
    public void updatePlanner(@RequestBody String content, @PathVariable UUID plannerId) {
        this.plannerService.updatePlanner(content, plannerId);
    }

    @PostMapping("/completed")
    public void getAllCompletedPlanner(){
        this.plannerService.getAllCompletedPlanner();
    }

    @PutMapping("/{plannerId}/complete")
    public void completePlannnerById(@PathVariable UUID plannerId){
        this.plannerService.completePlannnerById(plannerId);
    }
}
