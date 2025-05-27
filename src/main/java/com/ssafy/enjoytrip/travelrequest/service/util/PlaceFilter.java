package com.ssafy.enjoytrip.travelrequest.service.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.travelrequest.dto.PlaceContext;
import com.ssafy.enjoytrip.travelrequest.service.planstructure.TimeBlock;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaceFilter {
	
	private final StayTimeCalculator stayTimeCalc;
	
    public Map<String, List<PlaceContext>> removeDuplicatesWithFixed(
            Map<String, List<PlaceContext>> selectedPlaces,
            List<PlaceContext> fixedContexts
    ) {
        // 1. 고정 경유지 placeId 추출
        Set<String> fixedPlaceIds = fixedContexts.stream()
                .map(PlaceContext::getPlaceId)
                .collect(Collectors.toSet());

        // 2. 카테고리별 장소 리스트에서 fixedId 제거
        Map<String, List<PlaceContext>> deduplicated = new HashMap<>();

        for (Map.Entry<String, List<PlaceContext>> entry : selectedPlaces.entrySet()) {
            List<PlaceContext> filtered = entry.getValue().stream()
                    .filter(place -> {
                        String pid = place.getPlaceId();
                        return pid == null || !fixedPlaceIds.contains(pid);
                    })
                    .collect(Collectors.toList());

            deduplicated.put(entry.getKey(), filtered);
        }

        return deduplicated;
    }
    
    public List<PlaceContext> filterByDistanceOpeningHours(
    		TimeBlock block,
    		PlaceContext prev,
    		PlaceContext next,
    		List<PlaceContext> candidates,
    		int travelStyle,
    		Map<String, Map<String, Duration>> distanceMatrix
    		) {
    	List<PlaceContext> result = new ArrayList<>();
    	
    	for (PlaceContext place : candidates) {
    		String fromId = (prev != null) ? prev.getPlaceId() : null;
    		String toId = (next != null) ? next.getPlaceId() : null;
    		String placeId = place.getPlaceId();
    		
    		Duration moveFromPrev = (fromId != null)
    			    ? distanceMatrix.getOrDefault(fromId, Collections.emptyMap()).getOrDefault(placeId, Duration.ZERO)
    			    : Duration.ZERO;

    		Duration moveToNext = (toId != null)
    			    ? distanceMatrix.getOrDefault(placeId, Collections.emptyMap()).getOrDefault(toId, Duration.ZERO)
    			    : Duration.ZERO;
    		
    		Duration stay = stayTimeCalc.stayTimeFor(place.getContentTypeId(), travelStyle);
    		LocalDateTime arrival = block.getStart().plus(moveFromPrev);
    		LocalDateTime departure = arrival.plus(stay);
    		
    		// 1. 블록 안에 들어오는지 확인
    		if (departure.plus(moveToNext).isAfter(block.getEnd())) continue;
    		
    		// 2. 다음 고정 장소보다 늦게 도착하면 탈락
    		if (next != null && departure.plus(moveToNext).isAfter(next.getStartTime())) continue;
    		
    		// 3. 운영 시간 체크
    		if (place.getOpeningHours() != null && !OpeningHoursUtil.isAvailableBetween(place.getOpeningHours(), arrival, departure)) continue;
    		
    		// 통과
    		place.setStartTime(arrival);
    		place.setEndTime(departure);
    		result.add(place);
    	}
    	
    	return result;
    }
}
