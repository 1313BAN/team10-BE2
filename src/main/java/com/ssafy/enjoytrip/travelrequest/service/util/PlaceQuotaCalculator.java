package com.ssafy.enjoytrip.travelrequest.service.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.travelrequest.dto.request.TravelRequestDTO;

@Component
public class PlaceQuotaCalculator {
    final int attractionPerDay = 4;
    final double lodgePerDay = 0.5;
    final int restaurantPerDay = 3;
    final int cafePerDay = 1;
    
    public Map<String, Integer> calculateQuotas(TravelRequestDTO request) {
    	// 추후 구체화
        int totalDays = request.getTotalDays();

        Map<String, Integer> result = new HashMap<>();
        result.put("attractions", attractionPerDay * totalDays);
        result.put("lodges", (int) (lodgePerDay * totalDays));
        result.put("restaurants", restaurantPerDay * totalDays);
        result.put("cafes", cafePerDay * totalDays);

        return result;
    }
}
