package com.ssafy.enjoytrip.google.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GooglePlacesCacheDTO {

	private String placeId;
	private int contentTypeId;
	private String name;
	private double latitude;
	private double longitude;
	private String address;
	private String image;
	private boolean isCafe;
	private Double rating;
	private Integer totalRatings;
}
