package com.ssafy.enjoytrip.travelrequest.service;

import com.ssafy.enjoytrip.travelrequest.dto.response.PlanDTO;

public interface IPlanService {
	void savePlan(PlanDTO planDTO);
	
	PlanDTO getPlanById(int planId);

	void deletePlan(int planId);
	
	void updatePlan(int planId, PlanDTO planDTO);
}