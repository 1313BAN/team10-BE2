package com.ssafy.enjoytrip.travelrequest.service.planstructure;

import java.time.LocalDateTime;

import com.ssafy.enjoytrip.travelrequest.dto.response.VisitDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeBlock {
	private LocalDateTime start;
	private LocalDateTime end;
	private boolean isFixed;
	private VisitDTO fixedVisit;
}
