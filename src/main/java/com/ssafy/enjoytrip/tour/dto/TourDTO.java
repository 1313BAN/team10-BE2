package com.ssafy.enjoytrip.tour.dto;

import com.ssafy.enjoytrip.travelrequest.dto.OpeningHoursDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDTO {
	private String addr1;
	private String addr2;
	private int contentId;
	private int contentTypeId;
	private String image;
	private String imageThumbnail;
	private double latitude;
	private double longitude;
	private String title;
	private String zipcode;
	private Double rating; // null 가능
	private Integer totalRatings; // null 가능
	private boolean isCafe;
	
	private String placeId;
	private OpeningHoursDTO openingHours;
}
