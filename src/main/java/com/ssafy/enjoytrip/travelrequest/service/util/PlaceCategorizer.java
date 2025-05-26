package com.ssafy.enjoytrip.travelrequest.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.tour.dto.TourDTO;

@Component
public class PlaceCategorizer {

	public Map<String, List<TourDTO>> categorize(List<TourDTO> allPlaces) {

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
		Map<String, List<TourDTO>> result = new HashMap<>();
		
		result.put("attractions", new ArrayList<TourDTO>()); // 관광지 + 문화시설 + 축제공연행사 + 레포츠
		result.put("lodges", new ArrayList<TourDTO>()); // 숙박
		result.put("shoppings", new ArrayList<TourDTO>()); // 쇼핑
		result.put("restaurants", new ArrayList<TourDTO>()); // 음식점
		result.put("cafes", new ArrayList<TourDTO>()); // 카페
		
        for (TourDTO place : allPlaces) {
            switch (place.getContentTypeId()) {
            case 25:
                break;
            case 32:
                result.get("lodges").add(place);
                break;
            case 38:
                result.get("shoppings").add(place);
                break;
            case 39:
                if (place.isCafe()) result.get("cafes").add(place);
                else result.get("restaurants").add(place);
                break;
            default:
                result.get("attractions").add(place);
            }
        }
        
        return result;
	}
}
