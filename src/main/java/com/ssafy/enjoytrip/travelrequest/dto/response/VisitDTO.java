package com.ssafy.enjoytrip.travelrequest.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDTO {
	
	private int id;
	private int dailyPlanId;
    private String placeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int visitOrder;
    
    // google_places 연동 필드
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private String image;
    private double rating;
    private int totalRatings;
    private int contentTypeId;
}