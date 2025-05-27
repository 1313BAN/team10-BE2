package com.ssafy.enjoytrip.google.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GooglePlacesDetailDTO {

	private Map<String, Object> details;
}
