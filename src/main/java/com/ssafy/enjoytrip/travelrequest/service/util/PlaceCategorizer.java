package com.ssafy.enjoytrip.travelrequest.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.travelrequest.dto.PlaceContext;

@Component
public class PlaceCategorizer {

	public Map<String, List<PlaceContext>> categorize(List<PlaceContext> contexts) {

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
		Map<String, List<PlaceContext>> result = new HashMap<>();
		
		result.put("attractions", new ArrayList<PlaceContext>()); // 관광지 + 문화시설 + 축제공연행사 + 레포츠
		result.put("lodges", new ArrayList<PlaceContext>()); // 숙박
		result.put("shoppings", new ArrayList<PlaceContext>()); // 쇼핑
		result.put("restaurants", new ArrayList<PlaceContext>()); // 음식점
		result.put("cafes", new ArrayList<PlaceContext>()); // 카페
		
        for (PlaceContext place : contexts) {
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
