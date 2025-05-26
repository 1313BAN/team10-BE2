package com.ssafy.enjoytrip.travelrequest.dto.request;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StopoverDTO {
	
	private int contentId;
	private int contentTypeId;
	private String placeId;
	private LocalDate date;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Duration stayDuration;
	private boolean isFixed;
}