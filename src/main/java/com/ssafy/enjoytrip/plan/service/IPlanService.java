package com.ssafy.enjoytrip.plan.service;

import java.util.List;

import com.ssafy.enjoytrip.travelrequest.dto.response.PlanDTO;

public interface IPlanService {
	void savePlan(PlanDTO planDTO);
	
	List<PlanDTO> getPlansByUserId(int userId);
	
	PlanDTO getPlanById(int planId);

	void deletePlan(int planId);
	
	void updatePlan(int planId, PlanDTO planDTO);
}