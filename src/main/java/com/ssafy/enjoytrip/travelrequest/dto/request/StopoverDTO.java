package com.ssafy.enjoytrip.travelrequest.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StopoverDTO {
	
	// Google Places API info
	private String placeId;
	private String name;
	private String formattedAddress;
	private double latitude;
	private double longitude;
	// 추가
	private String image;
	private List<String> types;
	private Double rating; // nullable
	private Integer totalRatings; // nullable
	
	// 요청자가 설정한 time constraint
	private LocalDate date;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	// 시간 제약 여부
	private boolean isFixed;
}