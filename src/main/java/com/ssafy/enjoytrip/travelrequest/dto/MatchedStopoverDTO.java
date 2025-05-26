package com.ssafy.enjoytrip.travelrequest.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ssafy.enjoytrip.tour.dto.TourDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchedStopoverDTO {
	
	private TourDTO place;
	
	private LocalDate date;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	private boolean isFixed;
}
