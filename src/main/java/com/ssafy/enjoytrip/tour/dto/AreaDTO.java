package com.ssafy.enjoytrip.tour.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AreaDTO {
	private int code;
	private String name;
}
