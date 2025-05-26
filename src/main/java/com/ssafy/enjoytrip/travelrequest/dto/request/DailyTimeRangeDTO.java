package com.ssafy.enjoytrip.travelrequest.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyTimeRangeDTO {
	
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
}