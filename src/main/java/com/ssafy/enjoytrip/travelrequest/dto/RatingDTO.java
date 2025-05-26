package com.ssafy.enjoytrip.travelrequest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingDTO {

	private double rating;
	private int totalRatings;
}
