package com.ssafy.enjoytrip.plan.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoytrip.plan.dao.IPlanDAO;
import com.ssafy.enjoytrip.travelrequest.dto.response.DailyPlanDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.PlanDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.VisitDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanService implements IPlanService {

    private final IPlanDAO planDAO;

    @Override
    public void savePlan(PlanDTO planDTO) {
        planDAO.insertPlan(planDTO);

        for (DailyPlanDTO daily : planDTO.getDailyPlans()) {
            daily.setPlanId(planDTO.getPlanId());
            planDAO.insertDailyPlan(daily);

            for (VisitDTO visit : daily.getVisits()) {
                visit.setDailyPlanId(daily.getDailyPlanId());
                planDAO.insertVisit(visit);
            }
        }
    }

    @Override
    public void updatePlan(int planId, PlanDTO planDTO) {
        // 1. 기존 plan 업데이트
        planDAO.updatePlan(planId, planDTO);

        // 2. 기존 dailyPlan 및 visit 삭제
        List<DailyPlanDTO> existingDailyPlans = planDAO.getDailyPlansByPlanId(planId);
        for (DailyPlanDTO daily : existingDailyPlans) {
            planDAO.deleteVisitsByDailyPlanId(daily.getDailyPlanId());
        }
        planDAO.deleteDailyPlanByPlanId(planId);

        // 3. 새로 전달된 dailyPlan + visit 삽입
        for (DailyPlanDTO daily : planDTO.getDailyPlans()) {
            daily.setPlanId(planId);
            planDAO.insertDailyPlan(daily);

            for (VisitDTO visit : daily.getVisits()) {
                visit.setDailyPlanId(daily.getDailyPlanId());
                planDAO.insertVisit(visit);
            }
        }
    }

    @Override
    public List<PlanDTO> getPlansByUserId(int userId) {
        return planDAO.getPlansByUserId(userId);
    }

    @Override
    public PlanDTO getPlanById(int planId) {
        PlanDTO plan = planDAO.getPlanById(planId);
        List<DailyPlanDTO> dailyPlans = planDAO.getDailyPlansByPlanId(planId);

        for (DailyPlanDTO daily : dailyPlans) {
            // JOIN된 visit + place 정보 가져오기
            List<VisitDTO> visits = planDAO.getVisitsWithPlaceByDailyPlanId(daily.getDailyPlanId());
            daily.setVisits(visits);
        }

        plan.setDailyPlans(dailyPlans);
        return plan;
    }

    @Override
    public void deletePlan(int planId) {
        List<DailyPlanDTO> dailyPlans = planDAO.getDailyPlansByPlanId(planId);
        for (DailyPlanDTO daily : dailyPlans) {
            planDAO.deleteVisitsByDailyPlanId(daily.getDailyPlanId());
        }
        planDAO.deleteDailyPlanByPlanId(planId);
        planDAO.deletePlan(planId);
    }
}