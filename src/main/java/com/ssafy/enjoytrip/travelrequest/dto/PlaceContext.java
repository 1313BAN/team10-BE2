package com.ssafy.enjoytrip.travelrequest.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceContext {
	
	// 기본 정보
	private String placeId;
	private String name;
	private Integer contentTypeId;
	private double latitude;
	private double longitude;
	private String address;
	private String image;
	
	// 추가 정보
	private Double rating;
	private Integer totalRatings;
	private boolean isCafe;
	private OpeningHoursDTO openingHours;

	// 요청 정보
	private LocalDate date;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private boolean isRequested;
	private boolean isFixed;
	
	// 점수
	private double score;
}
