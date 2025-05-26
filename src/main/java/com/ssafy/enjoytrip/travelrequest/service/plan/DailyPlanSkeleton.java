package com.ssafy.enjoytrip.travelrequest.service.plan;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyPlanSkeleton {
	
	private LocalDate date;
	private List<TimeBlock> blocks;
}
