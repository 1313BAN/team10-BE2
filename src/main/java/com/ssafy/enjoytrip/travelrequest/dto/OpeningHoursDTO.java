package com.ssafy.enjoytrip.travelrequest.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpeningHoursDTO {

	private List<OpeningPeriodDTO> periods;
}
