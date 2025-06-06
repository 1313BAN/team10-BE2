package com.ssafy.enjoytrip.travelrequest.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDTO {
	private int planId;
    private int userId;
    private String title;
    private String sidoName;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DailyPlanDTO> dailyPlans;
}