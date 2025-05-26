package com.ssafy.enjoytrip.travelrequest.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoytrip.travelrequest.dao.IPlanDAO;
import com.ssafy.enjoytrip.travelrequest.dto.response.DailyPlanDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.PlanDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.VisitDTO;
import com.ssafy.enjoytrip.travelrequest.entity.DailyPlan;
import com.ssafy.enjoytrip.travelrequest.entity.Plan;
import com.ssafy.enjoytrip.travelrequest.entity.Visit;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanService implements IPlanService {

    private final IPlanDAO planDAO;

    @Override
    @Transactional
    public void savePlan(PlanDTO planDto) {
        Plan plan = Plan.builder()
            .userId(planDto.getUserId())
            .title(planDto.getTitle())
            .build();
        planDAO.insertPlan(plan);
        int planId = plan.getId();

        for (DailyPlanDTO dailyPlanDto : planDto.getDailyPlans()) {
            DailyPlan dailyPlan = DailyPlan.builder()
                .planId(planId)
                .date(dailyPlanDto.getDate())
                .build();
            planDAO.insertCourse(dailyPlan);
            int dailyPlanId = dailyPlan.getId();

            int order = 1;
            for (VisitDTO visitDto : dailyPlanDto.getVisits()) {
                Visit visit = Visit.builder()
                    .dailyPlanId(dailyPlanId)
                    .placeId(visitDto.getPlaceId())
                    .contentId(visitDto.getContentId())
                    .startTime(visitDto.getStartTime())
                    .endTime(visitDto.getEndTime())
                    .order(order++)
                    .build();
                planDAO.insertCourseDetail(visit);
            }
        }
    }

    @Override
    public PlanDTO getPlanById(int planId) {
        Plan plan = planDAO.getPlanById(planId);
        List<DailyPlan> dailyPlans = planDAO.getCoursesByPlanId(planId);

        List<DailyPlanDTO> dailyPlanDtos = dailyPlans.stream()
            .map(dailyPlan -> {
                List<Visit> visits = planDAO.getDetailsByCourseId(dailyPlan.getId());

                List<VisitDTO> visitDtos = visits.stream()
                    .map(visit -> VisitDTO.builder()
                        .visitId(visit.getId())
                        .placeId(visit.getPlaceId())
                        .contentId(visit.getContentId())
                        .startTime(visit.getStartTime())
                        .endTime(visit.getEndTime())
                        .order(visit.getOrder())
                        .build())
                    .collect(Collectors.toList());

                return DailyPlanDTO.builder()
                    .dailyPlanId(dailyPlan.getId())
                    .date(dailyPlan.getDate())
                    .visits(visitDtos)
                    .build();
            })
            .collect(Collectors.toList());

        return PlanDTO.builder()
            .planId(plan.getId())
            .userId(plan.getUserId())
            .title(plan.getTitle())
            .dailyPlans(dailyPlanDtos)
            .build();
    }

    @Override
    @Transactional
    public void updatePlan(int planId, PlanDTO planDto) {
        planDAO.updatePlanTitle(planId, planDto.getTitle());

        List<DailyPlan> existingPlans = planDAO.getCoursesByPlanId(planId);
        for (DailyPlan dailyPlan : existingPlans) {
            planDAO.deleteCourseDetailsByCourseId(dailyPlan.getId());
        }
        planDAO.deleteCoursesByPlanId(planId);

        for (DailyPlanDTO dailyPlanDto : planDto.getDailyPlans()) {
            DailyPlan dailyPlan = DailyPlan.builder()
                .planId(planId)
                .date(dailyPlanDto.getDate())
                .build();
            planDAO.insertCourse(dailyPlan);
            int dailyPlanId = dailyPlan.getId();

            int order = 1;
            for (VisitDTO visitDto : dailyPlanDto.getVisits()) {
                Visit visit = Visit.builder()
                    .dailyPlanId(dailyPlanId)
                    .placeId(visitDto.getPlaceId())
                    .contentId(visitDto.getContentId())
                    .startTime(visitDto.getStartTime())
                    .endTime(visitDto.getEndTime())
                    .order(order++)
                    .build();
                planDAO.insertCourseDetail(visit);
            }
        }
    }

    @Override
    public void deletePlan(int planId) {
        planDAO.deletePlan(planId);
    }

}