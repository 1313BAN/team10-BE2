package com.ssafy.enjoytrip.travelrequest.service.util;

import java.time.Duration;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class StayTimeCalculator {

	/*
	 * 0: 느긋
	 * 1: 빠듯
	 */
	private final double[] WEIGHT = {1.5, 0.8};
	private final Map<Integer, Integer> DEFAULT_DURATION = Map.of(
			12, 120, // 관광지
			14, 120, // 문화시설
			15, 120, // 행사
			28, 120, // 레포츠
			38, 120, // 쇼핑
			39, 60 // 음식점 or 카페
			);
	
	public Duration stayTimeFor(int contentTypeId, int travelStyle) {
		// 숙소는 skip
		if (contentTypeId == 32) return Duration.ZERO;
		
		int baseMinutes = DEFAULT_DURATION.get(contentTypeId);
		
		return Duration.ofMinutes((long) (baseMinutes * WEIGHT[travelStyle]));
	}
}
