package com.ssafy.enjoytrip.travelrequest.service.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.travelrequest.dto.PlaceContext;
import com.ssafy.enjoytrip.travelrequest.dto.request.TravelRequestDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaceSelector {
	
    final double BOUND = 0.4;
    
	private final PlaceScorer scorer;
	private final PlaceQuotaCalculator quotaCalculator;
//    * 3. 점수 매기고 정렬하기
//    * 4. 일단 총 날짜만 단순하게 고려해서 필요한 장소 개수 구하기
//    * 5. 점수 분포 상위 장소만 추리기. 필요한 장소의 1배수 이상으로
//    * 랜덤성 확보를 위해 상위 40%를 넘지 않는 선에서 필요한 장소의 2배수까지 개수 늘리고 shuffle
	public Map<String, List<PlaceContext>> scoreAndSelect(
			Map<String, List<PlaceContext>> categorizedPlaces, 
			TravelRequestDTO request) {
		
		scoreAndSort(categorizedPlaces, request.getPreferredTypes());
		Map<String, List<PlaceContext>> selectedAndShuffled = selectAndShuffle(categorizedPlaces, request);
		
		return selectedAndShuffled;
	}
	
	private void scoreAndSort(
			Map<String, List<PlaceContext>> categorizedPlaces, 
			List<Integer> preferredTypes) {
		
		for (Map.Entry<String, List<PlaceContext>> entry : categorizedPlaces.entrySet()) {
			String category = entry.getKey();
			List<PlaceContext> places = entry.getValue();
			
			if (category.equals("attractions") && preferredTypes != null) {
				scorer.scorePlaces(places, preferredTypes);
			} else {
				scorer.scorePlaces(places);
			}
			
			places.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
		}
	}
	
	private Map<String, List<PlaceContext>> selectAndShuffle(
			Map<String, List<PlaceContext>> scoredAndSorted,
			TravelRequestDTO request) {

		Map<String, Integer> quotas = quotaCalculator.calculateQuotas(request);
		Map<String, List<PlaceContext>> result = new HashMap<>();
		
		for (Map.Entry<String, List<PlaceContext>> entry : scoredAndSorted.entrySet()) {
			String category = entry.getKey();
			List<PlaceContext> places = entry.getValue();
			
			int size = places.size();
			int quota = quotas.get(category);
			
			List<PlaceContext> selected;
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
