package com.ssafy.enjoytrip.travelrequest.service.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.travelrequest.dto.MatchedStopoverDTO;
import com.ssafy.enjoytrip.travelrequest.dto.ScoredPlaceDTO;
import com.ssafy.enjoytrip.travelrequest.dto.TravelContextDTO;
import com.ssafy.enjoytrip.travelrequest.dto.request.TravelRequestDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TravelRequestProcessor {
	
	private final StopoverProcessor stopoverProcessor;

	public TravelContextDTO buildContext(
			TravelRequestDTO request,
			Map<String, List<ScoredPlaceDTO>> selectedPlaces,
			Map<Integer, Map<Integer, Integer>> distanceMatrix) {
		
		// 1. DB 조회하여 Stopover 세부 정보 얻어오기
		List<MatchedStopoverDTO> resolvedStopovers = stopoverProcessor.resolveStopovers(request.getStopovers());
		
		// 2. 경유지도 distanceMatrix에 포함
		// 3. 경유지 included set에 추가
		Set<Integer> included = new HashSet<>();
		for (MatchedStopoverDTO stopover : resolvedStopovers) {
			TourDTO place = stopover.getPlace();
			included.add(place.getContentId());
			
			if (distanceMatrix.containsKey(place.getContentId())) continue;
			else {
				Map<Integer, Integer> distances = new HashMap<>();
				
				for (Integer toId : distanceMatrix.keySet()) {
					
				}
				distanceMatrix.put(place.getContentId(), new HashMap<Integer, Integer>());
			}
		}
	}
}
