package com.ssafy.enjoytrip.plan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoytrip.plan.service.IPlanService;
import com.ssafy.enjoytrip.travelrequest.dto.response.PlanDTO;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    @Autowired
    private IPlanService planService;

    // 1. 계획 생성
    @PostMapping
    public ResponseEntity<String> createPlan(@RequestBody PlanDTO planDTO) {
        planService.savePlan(planDTO);
        return ResponseEntity.ok("계획이 성공적으로 저장되었습니다.");
    }

    // 2. 특정 사용자 ID로 계획 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlanDTO>> getPlansByUser(@PathVariable int userId) {
        List<PlanDTO> plans = planService.getPlansByUserId(userId);
        return ResponseEntity.ok(plans);
    }

    // 3. 단일 계획 상세 조회
    @GetMapping("/{planId}")
    public ResponseEntity<PlanDTO> getPlanById(@PathVariable int planId) {
        PlanDTO plan = planService.getPlanById(planId);
        if (plan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(plan);
    }

    // 4. 계획 수정
    @PutMapping("/{planId}")
    public ResponseEntity<String> updatePlan(@PathVariable int planId, @RequestBody PlanDTO planDTO) {
        planService.updatePlan(planId, planDTO);
        return ResponseEntity.ok("계획이 성공적으로 수정되었습니다.");
    }

    // 5. 계획 삭제
    @DeleteMapping("/{planId}")
    public ResponseEntity<String> deletePlan(@PathVariable int planId) {
        planService.deletePlan(planId);
        return ResponseEntity.ok("계획이 성공적으로 삭제되었습니다.");
    }
}

