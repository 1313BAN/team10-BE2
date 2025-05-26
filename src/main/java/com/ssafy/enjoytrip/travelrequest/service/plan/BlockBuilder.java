package com.ssafy.enjoytrip.travelrequest.service.plan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.ssafy.enjoytrip.travelrequest.dto.request.DailyTimeRangeDTO;
import com.ssafy.enjoytrip.travelrequest.dto.request.StopoverDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.VisitDTO;

public class BlockBuilder {
	
	public DailyPlanSkeleton build(
			LocalDate date,
			DailyTimeRangeDTO timeRange,
			List<StopoverDTO> stopovers) {
		
		LocalDateTime dayStart = LocalDateTime.of(date, timeRange.getStartTime());
		LocalDateTime dayEnd = LocalDateTime.of(date, timeRange.getEndTime());
		
		// 1. 경유지 중 해당 날짜에 도착/출발이 있는 애만 추출
		List<StopoverDTO> sorted = stopovers.stream()
				.filter(s -> !s.getStartTime().toLocalDate().isAfter(date) && !s.getEndTime().toLocalDate().isBefore(date))
				.sorted(Comparator.comparing(StopoverDTO::getStartTime))
				.collect(Collectors.toList());
		
		// 2. 시간 구간 쪼개기
		List<TimeBlock> blocks = new ArrayList<>();
		LocalDateTime currentStart = dayStart;
		
		VisitDTO previousVisit = null;
		
		for (StopoverDTO stop : sorted) {
			LocalDateTime arrival = stop.getStartTime();
			LocalDateTime departure = stop.getEndTime();
			
			// 구간 1: currentStart ~ stop.arrival
			if (currentStart.isBefore(arrival)) {
				blocks.add(new TimeBlock(
						currentStart, arrival,
						previousVisit != null, true,
						previousVisit, new VisitDTO(stop.toScoredPlace(), arrival, departure)
						));
			}
		}
		
		return null;
	}
}
