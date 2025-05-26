package com.ssafy.enjoytrip.travelrequest.service.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.travelrequest.dto.ScoredPlaceDTO;
import com.ssafy.enjoytrip.travelrequest.dto.request.TravelRequestDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaceSelector {
	
    final double BOUND = 0.4;
    
	private final PlaceScorer scorer;
	private final PlaceQuotaCalculator quotaCalculator;
	
	public Map<String, List<ScoredPlaceDTO>> scoreAndSelect(
			Map<String, List<TourDTO>> categorizedPlaces, 
			TravelRequestDTO request) {
		
		Map<String, List<ScoredPlaceDTO>> scoredAndSorted = scoreAndSort(categorizedPlaces, request.getPreferredTypes());
		Map<String, List<ScoredPlaceDTO>> selectedAndShuffled = selectAndShuffle(scoredAndSorted, request);
		
		return selectedAndShuffled;
	}
	
	private Map<String, List<ScoredPlaceDTO>> scoreAndSort(
			Map<String, List<TourDTO>> categorizedPlaces, 
			List<Integer> preferredTypes) {
		Map<String, List<ScoredPlaceDTO>> result = new HashMap<>();
		
		for (Map.Entry<String, List<TourDTO>> entry : categorizedPlaces.entrySet()) {
			String category = entry.getKey();
			List<TourDTO> places = entry.getValue();
			
			List<ScoredPlaceDTO> scored;
			if (category.equals("attractions") && preferredTypes != null) {
				scored = scorer.scorePlaces(places, preferredTypes);
			} else {
				scored = scorer.scorePlaces(places);
			}
			
			Collections.sort(scored);
			
			result.put(category, scored);
		}
		return result;
	}
	
	private Map<String, List<ScoredPlaceDTO>> selectAndShuffle(
			Map<String, List<ScoredPlaceDTO>> scoredAndSorted,
			TravelRequestDTO request) {

		Map<String, Integer> quotas = quotaCalculator.calculateQuotas(request);
		Map<String, List<ScoredPlaceDTO>> result = new HashMap<>();
		
		for (Map.Entry<String, List<ScoredPlaceDTO>> entry : scoredAndSorted.entrySet()) {
			String category = entry.getKey();
			List<ScoredPlaceDTO> places = entry.getValue();
			
			int size = places.size();
			int quota = quotas.get(category);
			
			List<ScoredPlaceDTO> selected;
			if (size < quota) {
				// 개수가 모자라면 그대로 반환
				selected = places;
			} else {
		        // 랜덤성 확보를 위해 상위 40%를 넘지 않는 선에서 필요한 장소의 2배수까지 개수 늘리기
				int topNPercent = 2 * quota;
		        topNPercent = Math.min((int) (BOUND * size), topNPercent);
		        topNPercent = Math.max(quota, topNPercent); // 필요 장소 1배수 이상
				
		        selected = new ArrayList<>(places.subList(0, topNPercent));
			}
			
			Collections.shuffle(selected);
			
			result.put(category, selected);
		}
        return result;
	}
}
