package com.ssafy.enjoytrip.travelrequest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpeningPeriodDTO {
	private int dayOfWeek; // 0: Sunday, 1: Monday, ..., 6: Saturday
	private String openTime;
	private String closeTime;
}
