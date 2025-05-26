package com.ssafy.enjoytrip.travelrequest.service.plan;

import java.time.LocalDateTime;

import com.ssafy.enjoytrip.travelrequest.dto.response.VisitDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeBlock {
	private LocalDateTime start;
	private LocalDateTime end;
	
	private boolean startFixed;
	private boolean endFixed;
	
	private VisitDTO fixedStartPlace; // nullable
	private VisitDTO fixedEndPlace; // nullable
	
	private BlockPurpose purpose = BlockPurpose.GENERAL;
	
	
}
