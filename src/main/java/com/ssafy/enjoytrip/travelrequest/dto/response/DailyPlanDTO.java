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
public class DailyPlanDTO {
	private int dailyPlanId;
    private LocalDate date;  // "yyyy-MM-dd" 형식
    private List<VisitDTO> visits;
}