package com.ssafy.enjoytrip.travelrequest.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ssafy.enjoytrip.travelrequest.dto.request.DailyTimeRangeDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelContextDTO {
	
	private int areaCode; // not null
	private Integer sigunguCode; // nullable
	
    /* 
     * DB 기준 장소 타입
     * 12: 관광지
     * 14: 문화시설
     * 15: 축제공연행사
     * 25: 여행코스
     * 28: 레포츠
     * 32: 숙박
     * 38: 쇼핑
     * 39: 음식점
     */
	private List<Integer> preferredTypes;
	
	private int totalDays;
	private List<DailyTimeRangeDTO> dailyTimeRanges;

	private List<PlaceContext> stopovers;
	
	/*
	 * 0: 느긋한 여행
	 * 1: 빠듯한 여행
	 */
	private int travleStyle;
	
	private Map<String, List<PlaceContext>> selectedPlaces;
	
	private Map<Integer, Map<Integer, Integer>> distanceMatrix;
	
	private Set<Integer> included;
}
