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
	private int visitId;
    private int placeId;
    private int contentId;
    private LocalDateTime startTime;  // "HH:mm" 형식
    private LocalDateTime endTime;
    private int order;
}