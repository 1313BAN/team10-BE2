package com.ssafy.enjoytrip.plan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.enjoytrip.travelrequest.dto.response.DailyPlanDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.PlanDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.VisitDTO;

@Mapper
public interface IPlanDAO {

    // Plan
    int insertPlan(PlanDTO plan);
    int updatePlan(@Param("planId") int planId, @Param("plan") PlanDTO plan);
    int deletePlan(@Param("planId") int planId);

    // DailyPlan
    int insertDailyPlan(DailyPlanDTO dailyPlan);
    int deleteDailyPlanByPlanId(@Param("planId") int planId);

    // Visit
    int insertVisit(VisitDTO visit);
    int deleteVisitsByDailyPlanId(@Param("dailyPlanId") int dailyPlanId);

    // 조회
    PlanDTO getPlanById(@Param("id") int id);
    List<PlanDTO> getPlansByUserId(@Param("userId") int userId);
    List<DailyPlanDTO> getDailyPlansByPlanId(@Param("planId") int planId);
    List<VisitDTO> getVisitsByDailyPlanId(@Param("dailyPlanId") int dailyPlanId);
    List<VisitDTO> getVisitsWithPlaceByDailyPlanId(@Param("dailyPlanId") int dailyPlanId);
}