package com.ssafy.enjoytrip.travelrequest.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.enjoytrip.travelrequest.entity.DailyPlan;
import com.ssafy.enjoytrip.travelrequest.entity.Plan;
import com.ssafy.enjoytrip.travelrequest.entity.Visit;

@Mapper
public interface IPlanDAO {
    
    // 1. 계획 저장
    int insertPlan(Plan plan);

    // 2. 날짜별 코스 저장
    int insertCourse(DailyPlan course);

    // 3. 상세 장소 저장
    int insertCourseDetail(Visit detail);

    // 4. 단일 계획 조회
    Plan getPlanById(@Param("id") int id);

    // 5. 계획에 포함된 코스들 조회
    List<DailyPlan> getCoursesByPlanId(@Param("planId") int planId);

    // 6. 특정 코스에 포함된 장소들 조회
    List<Visit> getDetailsByCourseId(@Param("courseId") int courseId);
    
    // 일정 삭제
    int deletePlan(@Param("planId") int planId);
    int deleteCoursesByPlanId(@Param("planId") int planId);
    int deleteCourseDetailsByCourseId(@Param("courseId") int courseId);
    
    // plan 이름 변경
    int updatePlanTitle(@Param("planId") int planId, @Param("title") String title);
}
